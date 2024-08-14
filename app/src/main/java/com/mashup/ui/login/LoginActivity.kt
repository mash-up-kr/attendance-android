package com.mashup.ui.login

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_LINK
import com.mashup.constant.EXTRA_LOGOUT
import com.mashup.constant.EXTRA_MAIN_TAB
import com.mashup.constant.EXTRA_WITH_DRAWL
import com.mashup.constant.log.LOG_LOGIN
import com.mashup.constant.log.LOG_SIGN_UP
import com.mashup.core.common.constant.MEMBER_NOT_FOUND
import com.mashup.core.common.constant.MEMBER_NOT_MATCH_PASSWORD
import com.mashup.core.common.extensions.onThrottleFirstClick
import com.mashup.core.common.model.ActivityEnterType
import com.mashup.core.common.model.Validation
import com.mashup.databinding.ActivityLoginBinding
import com.mashup.service.PushLinkType
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.main.MainActivity
import com.mashup.ui.main.model.MainTab
import com.mashup.ui.password.PasswordActivity
import com.mashup.ui.qrscan.QRScanActivity
import com.mashup.ui.signup.SignUpActivity
import com.mashup.ui.webview.birthday.BirthdayActivity
import com.mashup.ui.webview.mashong.MashongActivity
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override fun initViews() {
        initLoginState()
        initTextField()
        initButtons()
    }

    private fun initLoginState() {
        val isRequestLogOut = intent.getBooleanExtra(EXTRA_LOGOUT, false)
        val isRequestWithDrawl = intent.getBooleanExtra(EXTRA_WITH_DRAWL, false)

        when {
            isRequestWithDrawl -> {
                showToast("회원탈퇴 완료되었어요")
            }

            isRequestLogOut -> {
                showToast("로그아웃 되었어요")
            }
        }
    }

    override fun initObserves() {
        viewModel.run {
            flowLifecycleScope {
                loginUiState.collect { state ->
                    when (state) {
                        is LoginState.Loading -> {
                            viewBinding.btnLogin.showLoading()
                        }

                        is LoginState.Success -> {
                            viewBinding.btnLogin.hideLoading()
                            moveNextScreen(state.loginType)
                        }

                        is LoginState.Error -> {
                            viewBinding.btnLogin.hideLoading()
                            handleCommonError(state.code)
                            handleSignUpErrorCode(state)
                        }

                        else -> {
                        }
                    }
                }
            }

            flowLifecycleScope {
                loginValidation.collectLatest {
                    viewBinding.btnLogin.setButtonEnabled(it == Validation.SUCCESS)
                }
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldId.run {
            addOnTextChangedListener { text ->
                viewModel.setId(text)
            }
        }

        viewBinding.textFieldPwd.run {
            addOnTextChangedListener { text ->
                viewModel.setPwd(text)
            }
        }
    }

    private fun initButtons() = with(viewBinding) {
        btnLogin.setOnButtonThrottleFirstClickListener(this@LoginActivity) {
            AnalyticsManager.addEvent(eventName = LOG_LOGIN)
            viewModel.requestLogin(
                id = textFieldId.inputtedText,
                pwd = textFieldPwd.inputtedText
            )
        }

        tvSignUp.onThrottleFirstClick(lifecycleScope) {
            AnalyticsManager.addEvent(eventName = LOG_SIGN_UP)
            startActivity(
                SignUpActivity.newIntent(this@LoginActivity)
            )
        }

        tvChangePassword.onThrottleFirstClick(lifecycleScope) {
            startActivity(PasswordActivity.newIntent(this@LoginActivity))
        }
    }

    private fun handleSignUpErrorCode(error: LoginState.Error) {
        val codeMessage = when (error.code) {
            MEMBER_NOT_FOUND -> {
                "회원을 찾을 수 없습니다."
            }

            MEMBER_NOT_MATCH_PASSWORD -> {
                "비밀번호가 일치하지 않습니다."
            }

            else -> {
                null
            }
        }
        codeMessage?.run { showToast(codeMessage) }
    }

    private fun moveNextScreen(loginType: LoginType) {
        val deepLink = intent.getStringExtra(EXTRA_LINK).orEmpty()
        val baseIntent = MainActivity.newIntent(
            context = this,
            loginType = loginType,
            mainTab = (intent.getSerializableExtra(EXTRA_MAIN_TAB) as? MainTab) ?: MainTab.EVENT
        )
        val taskStackBuilder = when (val pushType = PushLinkType.getPushLinkType(deepLink)) {
            PushLinkType.DANGGN, PushLinkType.DANGGN_REWARD -> {
                buildTaskStack(
                    baseIntent,
                    ShakeDanggnActivity.newIntent(
                        context = this,
                        showDanggnRewardNotice = pushType == PushLinkType.DANGGN_REWARD,
                        type = ActivityEnterType.ALARM
                    )
                )
            }

            PushLinkType.BIRTHDAY -> {
                buildTaskStack(baseIntent, BirthdayActivity.newIntent(this))
            }

            PushLinkType.MASHONG -> {
                buildTaskStack(baseIntent, MashongActivity.newIntent(this))
            }

            PushLinkType.QR -> {
                buildTaskStack(baseIntent, QRScanActivity.newIntent(this))
            }

            else -> {
                buildTaskStack(baseIntent)
            }
        }
        taskStackBuilder.startActivities()
        finish()
    }

    private fun buildTaskStack(baseIntent: Intent, vararg newIntent: Intent) =
        TaskStackBuilder.create(this)
            .apply {
                addNextIntentWithParentStack(baseIntent)
                newIntent.forEach { intent ->
                    addNextIntent(intent)
                }
            }

    override val layoutId: Int = R.layout.activity_login

    companion object {

        fun newIntent(
            context: Context,
            isLogout: Boolean = false,
            isWithDrawl: Boolean = false,
            mainTab: MainTab = MainTab.EVENT,
            deepLink: String = PushLinkType.UNKNOWN.name
        ): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(EXTRA_LOGOUT, isLogout)
                putExtra(EXTRA_WITH_DRAWL, isWithDrawl)
                putExtra(EXTRA_MAIN_TAB, mainTab)
                putExtra(EXTRA_LINK, deepLink)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }
}
