package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.URL
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.log.LOG_SETTING_DELETE_USER
import com.mashup.constant.log.LOG_SETTING_LOGOUT
import com.mashup.constant.log.LOG_SETTING_SNS_FACEBOOK
import com.mashup.constant.log.LOG_SETTING_SNS_INSTAGRAM
import com.mashup.constant.log.LOG_SETTING_SNS_MASHUP_HOME
import com.mashup.constant.log.LOG_SETTING_SNS_MASHUP_RECRUIT
import com.mashup.constant.log.LOG_SETTING_SNS_TISTORY
import com.mashup.constant.log.LOG_SETTING_SNS_YOUTUBE
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.widget.CommonDialog
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivitySettingBinding
import com.mashup.feature.setting.SettingScreen
import com.mashup.ui.login.LoginActivity
import com.mashup.ui.withdrawl.WithdrawalActivity
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        viewBinding.settingScreen.setContent {
            MashUpTheme {
                SettingScreen(
                    modifier = Modifier.fillMaxSize(),
                    onLogout = this::onClickLogoutButton,
                    onDeleteUser = this::moveToDeleteAccount,
                    onClickSNS = this::onClickSNS,
                    onClickPush = this::moveToPushActivity,
                    onClickBackButton = this::onBackPressed
                )
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.onSuccessLogout.collectLatest {
                moveToLoginActivityOnLogout()
            }
        }
    }

    private fun onClickLogoutButton() {
        AnalyticsManager.addEvent(LOG_SETTING_LOGOUT)
        showLogoutDialog()
    }

    private fun showLogoutDialog() {
        CommonDialog(this).apply {
            setTitle(text = "로그아웃 하시겠습니까?")
            setNegativeButton()
            setPositiveButton {
                viewModel.requestLogout()
            }
            show()
        }
    }

    private fun moveToLoginActivityOnLogout() {
        finish()
        startActivity(
            LoginActivity.newIntent(
                context = this@SettingActivity,
                isLogout = true
            )
        )
    }

    private fun moveToPushActivity() {
        startActivity(
            PushActivity.newIntent(
                context = this@SettingActivity
            )
        )
    }

    private fun moveToDeleteAccount() {
        AnalyticsManager.addEvent(LOG_SETTING_DELETE_USER)
        startActivity(
            WithdrawalActivity.newInstance(this)
        )
    }

    private fun onClickSNS(link: String) {
        val eventLog = when (link) {
            URL.FACEBOOK -> LOG_SETTING_SNS_FACEBOOK
            URL.INSTAGRAM -> LOG_SETTING_SNS_INSTAGRAM
            URL.TISTORY -> LOG_SETTING_SNS_TISTORY
            URL.YOUTUBE -> LOG_SETTING_SNS_YOUTUBE
            URL.MASHUP_UP_HOME -> LOG_SETTING_SNS_MASHUP_HOME
            URL.MASHUP_UP_RECRUIT -> LOG_SETTING_SNS_MASHUP_RECRUIT
            else -> null
        }
        eventLog?.run { AnalyticsManager.addEvent(this) }
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
        }
    }

    override val layoutId = R.layout.activity_setting
}
