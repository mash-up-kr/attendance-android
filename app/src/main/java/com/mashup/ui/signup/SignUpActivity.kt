package com.mashup.ui.signup

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.log.KEY_PLACE
import com.mashup.constant.log.LOG_COMMON_BACK
import com.mashup.constant.log.LOG_COMMON_CLOSE
import com.mashup.constant.log.LOG_PLACE_SIGN_CODE
import com.mashup.constant.log.LOG_PLACE_SIGN_MEMBER_INFO
import com.mashup.constant.log.LOG_PLACE_SIGN_PLATFORM
import com.mashup.constant.log.LOG_POPUP_SIGNUP_CANCEL
import com.mashup.constant.log.LOG_POPUP_SIGNUP_CONFIRM
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.widget.CommonDialog
import com.mashup.databinding.ActivitySignUpBinding
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    private val viewModel: SignUpViewModel by viewModels()

    private var navigationAnimationType = NavigationAnimationType.SLIDE

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun initViews() {
        initToolbar()
    }

    private fun initToolbar() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            navigationAnimationType = NavigationAnimationType.SLIDE
            onBackPressed()
        }
        viewBinding.toolbar.setOnCloseButtonClickListener {
            getPlaceGALog()?.run {
                AnalyticsManager.addEvent(
                    LOG_COMMON_CLOSE,
                    bundleOf(KEY_PLACE to this)
                )
            }
            navigationAnimationType = NavigationAnimationType.PULL
            if (!viewModel.isDataEmpty()) {
                showExitPopup {
                    finish()
                }
            } else {
                finish()
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.showToolbarDivider.collectLatest { isVisible ->
                if (isVisible) {
                    viewBinding.toolbar.showDivider()
                } else {
                    viewBinding.toolbar.hideDivider()
                }
            }
        }

        flowLifecycleScope {
            viewModel.showToolbarClose.collectLatest { isVisible ->
                viewBinding.toolbar.setVisibleCloseButton(isVisible)
            }
        }
    }

    override fun onBackPressed() {
        getPlaceGALog()?.run {
            AnalyticsManager.addEvent(
                LOG_COMMON_BACK,
                bundleOf(KEY_PLACE to this)
            )
        }
        if (!navController.popBackStack()) {
            finish()
        }
    }

    private fun showExitPopup(action: () -> Unit) {
        CommonDialog(this).apply {
            setTitle(text = "회원가입을 그만두시겠어요?")
            setMessage(text = "입력한 전체 내용이 삭제됩니다.")
            setNegativeButton {
                AnalyticsManager.addEvent(eventName = LOG_POPUP_SIGNUP_CANCEL)
            }
            setPositiveButton {
                AnalyticsManager.addEvent(eventName = LOG_POPUP_SIGNUP_CONFIRM)
                action()
            }
            show()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            0,
            navigationAnimationType.exitOut
        )
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SignUpActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
        }
    }

    private fun getPlaceGALog() = when (navController.currentDestination?.id) {
        R.id.signUpCodeFragment -> LOG_PLACE_SIGN_CODE
        R.id.signUpMemberFragment -> LOG_PLACE_SIGN_PLATFORM
        R.id.signUpAuthFragment -> LOG_PLACE_SIGN_MEMBER_INFO
        else -> null
    }

    override val layoutId: Int
        get() = R.layout.activity_sign_up
}
