package com.mashup.ui.mypage

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.core.common.utils.ToastUtil
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityMyProfileEditBinding
import com.mashup.feature.mypage.profile.LoadState
import com.mashup.feature.mypage.profile.MyPageProfileEditViewModel
import com.mashup.feature.mypage.profile.edit.MyPageEditCardScreen
import com.mashup.feature.mypage.profile.edit.MyPageEditProfileScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyProfileEditActivity : BaseActivity<ActivityMyProfileEditBinding>() {
    override val layoutId = R.layout.activity_my_profile_edit
    private val editViewModel: MyPageProfileEditViewModel by viewModels()

    private var editType: Int = TYPE_EDIT_PROFILE_INTRODUCE

    override fun initViews() {
        super.initViews()

        editType = intent.getIntExtra(EXTRA_EDIT_TYPE, TYPE_EDIT_PROFILE_INTRODUCE)

        viewBinding.viewCompose.setContent {
            MashUpTheme {
                when (editType) {
                    TYPE_EDIT_PROFILE_INTRODUCE -> {
                        MyPageEditProfileScreen(
                            viewModel = editViewModel,
                            onBackPressed = ::finish
                        )
                    }
                    TYPE_EDIT_PROFILE_CARD -> {
                        MyPageEditCardScreen(
                            viewModel = editViewModel,
                            onBackPressed = ::finish
                        )
                    }
                }
            }
        }

        setObserver()
    }

    private fun setObserver() {
        flowLifecycleScope {
            editViewModel.loadState.collectLatest {
                if (it is LoadState.Loaded) {
                    ToastUtil.showToast(this@MyProfileEditActivity, "저장 완료!")

                    if (editType == TYPE_EDIT_PROFILE_INTRODUCE) {
                        setResult(RESULT_OK)
                    }
                }
            }
        }

        flowLifecycleScope {
            editViewModel.myProfileCard.collectLatest {
                val updatedIntent = Intent().apply {
                    putExtra(MyProfileCardEditActivity.EXTRA_CARD_TEAM, it.team)
                    putExtra(MyProfileCardEditActivity.EXTRA_CARD_STAFF, it.staff)
                }

                setResult(RESULT_OK, updatedIntent)
            }
        }

        flowLifecycleScope {
            editViewModel.invalidInputError.collectLatest { errorMessage ->
                ToastUtil.showToast(this@MyProfileEditActivity, errorMessage)
            }
        }
    }

    companion object {
        private const val EXTRA_EDIT_TYPE = "EXTRA_EDIT_TYPE"
        const val TYPE_EDIT_PROFILE_INTRODUCE = 1
        const val TYPE_EDIT_PROFILE_CARD = 2

        fun newIntent(context: Context, editType: Int): Intent {
            return Intent(context, MyProfileEditActivity::class.java).apply {
                putExtra(EXTRA_EDIT_TYPE, editType)
            }
        }
    }
}
