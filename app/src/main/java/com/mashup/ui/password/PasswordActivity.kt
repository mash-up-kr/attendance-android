package com.mashup.ui.password

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.log.KEY_PLACE
import com.mashup.constant.log.LOG_COMMON_BACK
import com.mashup.constant.log.LOG_PLACE_CHANGE_PASSWORD
import com.mashup.constant.log.LOG_PLACE_ENTER_ID
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.databinding.ActivityPasswordBinding
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordActivity : BaseActivity<ActivityPasswordBinding>() {

    private val passwordViewModel: PasswordViewModel by viewModels()

    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_password_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun initViews() {
        initToolbar()
    }

    private fun initToolbar() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            onBackPressed()
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
        } else {
            // 비밀번호 변경화면에서 뒤로 돌아갈때도 Button이 활성화 되도록 함.
            passwordViewModel.updateButtonState(ButtonState.Enable)
        }
    }

    private fun getPlaceGALog() = when (navController.currentDestination?.id) {
        R.id.changePasswordFragment -> LOG_PLACE_CHANGE_PASSWORD
        R.id.enterIdFragment -> LOG_PLACE_ENTER_ID
        else -> null
    }

    override val layoutId: Int
        get() = R.layout.activity_password

    companion object {
        fun newIntent(context: Context) = Intent(context, PasswordActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
        }
    }
}
