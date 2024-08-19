package com.mashup.ui.withdrawl

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.log.LOG_DELETE_USER_SUCCESS
import com.mashup.core.common.extensions.setEmptyUIOfTextField
import com.mashup.core.common.extensions.setFailedUiOfTextField
import com.mashup.core.common.extensions.setSuccessUiOfTextField
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.model.Validation
import com.mashup.core.common.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import com.mashup.databinding.ActivityWithdrawalBinding
import com.mashup.ui.login.LoginActivity
import com.mashup.ui.signup.state.CodeState
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class WithdrawalActivity : BaseActivity<ActivityWithdrawalBinding>() {

    private val viewModel: WithdrawalViewModel by viewModels()

    override fun initViews() {
        initToolbar()
        initTextField()
        initButton()
        initDescription()
    }

    private fun initToolbar() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            finish()
        }
    }

    private fun initButton() {
        ViewCompat.setWindowInsetsAnimationCallback(
            viewBinding.layoutButton,
            TranslateDeferringInsetsAnimationCallback(
                view = viewBinding.layoutButton,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )
        viewBinding.btnWithdrawal.setButtonEnabled(false)
        viewBinding.btnWithdrawal.setOnButtonThrottleFirstClickListener(this) {
            viewModel.deleteMember()
        }
    }

    private fun initTextField() {
        viewBinding.textFieldCode.run {
            addOnTextChangedListener { text ->
                viewModel.setCode(text)
            }
        }
    }

    private fun initDescription() {
        viewBinding.tvDescription.text =
            getString(R.string.sign_out_msg_detail).replace(" ", "\u00A0")
    }

    override fun initObserves() = with(viewModel) {
        flowLifecycleScope {
            codeState.collectLatest {
                setUiOfCodeState(it)
            }
        }
        flowLifecycleScope {
            withdrawalState.collectLatest { state ->
                when (state) {
                    is WithdrawalState.Loading -> {
                        showLoading()
                    }
                    is WithdrawalState.Error -> {
                        hideLoading()
                        handleCommonError(state.code)
                    }
                    is WithdrawalState.Success -> {
                        hideLoading()
                        AnalyticsManager.addEvent(LOG_DELETE_USER_SUCCESS)
                        finish()
                        startActivity(
                            LoginActivity.newIntent(
                                context = this@WithdrawalActivity,
                                isWithDrawl = true
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setUiOfCodeState(codeState: CodeState) {
        with(viewBinding.textFieldCode) {
            when (codeState.validationCode) {
                Validation.SUCCESS -> {
                    setDescriptionText("위 문구를 입력해주세요.")
                    setSuccessUiOfTextField()
                }
                Validation.FAILED -> {
                    setDescriptionText("문구가 동일하지 않아요")
                    setFailedUiOfTextField()
                }
                Validation.EMPTY -> {
                    setDescriptionText("위 문구를 입력해주세요.")
                    setEmptyUIOfTextField()
                }
            }
        }
        viewBinding.btnWithdrawal.setButtonEnabled(codeState.isValidationState)
    }

    companion object {
        fun newInstance(context: Context) =
            Intent(context, WithdrawalActivity::class.java).apply {
                putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
            }
    }

    override val layoutId: Int = R.layout.activity_withdrawal
}
