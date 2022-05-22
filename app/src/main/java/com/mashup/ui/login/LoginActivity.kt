package com.mashup.ui.login

import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityLoginBinding
import com.mashup.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override fun initObserves() {
        viewModel.run {
            flowLifecycleScope {
                loginUiState.collect { state ->
                    when (state) {
                        LoginUiState.LoginSuccess -> {
                            moveToMainScreen()
                        }
                    }
                }
            }
        }
    }

    private fun moveToMainScreen() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
        finish()
    }

    override val layoutId: Int
        get() = R.layout.activity_login
}