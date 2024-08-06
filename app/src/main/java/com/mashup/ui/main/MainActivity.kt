package com.mashup.ui.main

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.EXTRA_LOGIN_TYPE
import com.mashup.constant.EXTRA_MAIN_TAB
import com.mashup.core.common.extensions.onThrottleFirstClick
import com.mashup.core.common.model.ActivityEnterType
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.utils.PermissionHelper
import com.mashup.core.common.utils.safeShow
import com.mashup.core.common.widget.CommonDialog
import com.mashup.databinding.ActivityMainBinding
import com.mashup.ui.danggn.DanggnInfoActivity
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.login.LoginType
import com.mashup.ui.main.model.MainPopupType
import com.mashup.ui.main.model.MainTab
import com.mashup.ui.main.popup.MainBottomPopup
import com.mashup.ui.qrscan.CongratsAttendanceScreen
import com.mashup.ui.qrscan.QRScanActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main

    private val viewModel: MainViewModel by viewModels()

    private val permissionHelper by lazy {
        PermissionHelper(this)
    }

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navHostFragment.navController
    }

    private val qrcodeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            QRScanActivity.RESULT_CONFIRM_SUCCESS_QR -> {
                viewModel.confirmAttendance()
                viewModel.successAttendance()
            }
            QRScanActivity.RESULT_CONFIRM_QR -> {
                viewModel.confirmAttendance()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
    }

    override fun initViews() {
        super.initViews()

        initComposeView()
        initTabButtons()
        requestNotificationPermission()
    }

    private fun initComposeView() {
        viewBinding.composeView.setContent {
            CongratsAttendanceScreen(
                isVisible = viewModel.isShowCongratsAttendanceScreen.value
            )
        }
    }

    private fun initTabButtons() = with(viewBinding) {
        layoutMainTab.btnQrcode.onThrottleFirstClick(lifecycleScope) {
            qrcodeLauncher.launch(
                QRScanActivity.newIntent(this@MainActivity)
            )
        }
        layoutMainTab.sectionEvent.onThrottleFirstClick(lifecycleScope) {
            viewModel.setMainTab(MainTab.EVENT)
        }
        layoutMainTab.sectionMyPage.onThrottleFirstClick(lifecycleScope) {
            viewModel.setMainTab(MainTab.MY_PAGE)
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            launch {
                viewModel.mainTab.collectLatest { tab ->
                    navigationTab(tab)
                    setUIOfTab(tab)
                }
            }

            launch {
                viewModel.showPopupType.collectLatest {
                    MainBottomPopup.newInstance(it).safeShow(supportFragmentManager)
                }
            }

            launch {
                viewModel.onClickPopupConfirm.collectLatest { popupType ->
                    when (popupType) {
                        MainPopupType.DANGGN -> {
                            viewModel.disablePopup(popupType)
                            startActivity(
                                ShakeDanggnActivity.newIntent(
                                    context = this@MainActivity,
                                    type = ActivityEnterType.POPUP
                                )
                            )
                        }

                        MainPopupType.DANGGN_UPDATE -> {
                            viewModel.disablePopup(popupType)
                            startActivity(
                                DanggnInfoActivity.newIntent(
                                    context = this@MainActivity
                                )
                            )
                        }

                        MainPopupType.BIRTHDAY_CELEBRATION -> {
                            viewModel.disablePopup(popupType)
                            // start Activity 추가
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }

    private fun navigationTab(toDestination: MainTab) {
        val currentNavigationId = navController.currentDestination?.id
        val newNavigationId = when (toDestination) {
            MainTab.EVENT -> {
                R.id.eventFragment
            }
            MainTab.MY_PAGE -> {
                R.id.myPageFragment
            }
        }
        if (currentNavigationId != newNavigationId) {
            navController.navigate(newNavigationId)
        }
    }

    private fun setUIOfTab(tab: MainTab) = with(viewBinding.layoutMainTab) {
        val selectedColor =
            ContextCompat.getColor(this@MainActivity, com.mashup.core.common.R.color.gray800)
        val selectedColorList = ContextCompat.getColorStateList(
            this@MainActivity,
            com.mashup.core.common.R.color.gray800
        )
        val unSelectedColor =
            ContextCompat.getColor(this@MainActivity, com.mashup.core.common.R.color.gray500)
        val unSelectedColorList =
            ContextCompat.getColorStateList(
                this@MainActivity,
                com.mashup.core.common.R.color.gray500
            )

        when (tab) {
            MainTab.EVENT -> {
                tvEvent.setTextColor(selectedColor)
                imgEvent.imageTintList = selectedColorList
                tvMyPage.setTextColor(unSelectedColor)
                imgMyPage.imageTintList = unSelectedColorList
            }
            MainTab.MY_PAGE -> {
                tvEvent.setTextColor(unSelectedColor)
                imgEvent.imageTintList = unSelectedColorList
                tvMyPage.setTextColor(selectedColor)
                imgMyPage.imageTintList = selectedColorList
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHelper.checkGrantedPermission(
                permission = requestPermission,
                onRequestPermission = {
                    permissionHelper.requestPermission(
                        permission = requestPermission
                    )
                },
                onShowRationaleUi = {
                    showRationalNotificationPermissionDialog()
                }
            )
        }
    }

    private fun showRationalNotificationPermissionDialog() {
        CommonDialog(this).apply {
            setTitle(text = "알림 권한 재설정")
            setMessage(text = "매쉬업 소식을 빠르게 알기 위해서는\n알림 권한이 필수로 필요해요.")
            setNegativeButton(text = "닫기")
            setPositiveButton("재설정") {
                permissionHelper.requestPermission(
                    permission = requestPermission
                )
            }
            show()
        }
    }

    companion object {
        @SuppressLint("InlinedApi")
        private const val requestPermission = POST_NOTIFICATIONS

        fun newIntent(
            context: Context,
            loginType: LoginType,
            mainTab: MainTab = MainTab.EVENT
        ) = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
            putExtra(EXTRA_LOGIN_TYPE, loginType)
            putExtra(EXTRA_MAIN_TAB, mainTab)
        }
    }
}
