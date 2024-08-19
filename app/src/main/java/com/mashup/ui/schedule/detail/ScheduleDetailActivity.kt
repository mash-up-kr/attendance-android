package com.mashup.ui.schedule.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.constant.EXTRA_SCHEDULE_TYPE
import com.mashup.constant.log.LOG_EVENT_LIST_ALL
import com.mashup.constant.log.LOG_EVENT_LIST_DETAIL_COPY
import com.mashup.core.common.constant.SCHEDULE_NOT_FOUND
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityScheduleDetailBinding
import com.mashup.ui.attendance.platform.PlatformAttendanceActivity
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import com.mashup.core.common.R as CR

@AndroidEntryPoint
class ScheduleDetailActivity : BaseActivity<ActivityScheduleDetailBinding>() {
    override val layoutId = R.layout.activity_schedule_detail

    private val viewModel: ScheduleDetailViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        setStatusBarColorRes(CR.color.white)
        viewBinding.composeView.setContent {
            val state by viewModel.scheduleState.collectAsState()

            MashUpTheme {
                ScheduleDetailScreen(
                    state = state,
                    isPlatformSeminar = viewModel.scheduleType != "ALL",
                    copyToClipboard = ::copyToClipboard,
                    moveToPlatformAttendance = ::moveToPlatformAttendance,
                    onBackPressed = { finish() }
                )
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.scheduleState.collectLatest { state ->
                when (state) {
                    ScheduleState.Loading -> {
                        showLoading()
                    }

                    is ScheduleState.Success -> {
                        hideLoading()
                    }

                    is ScheduleState.Error -> {
                        hideLoading()
                        handleCommonError(state.code)
                        handleScheduleDetailErrorCode(state)
                    }

                    else -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun handleScheduleDetailErrorCode(error: ScheduleState.Error) {
        val codeMessage = when (error.code) {
            SCHEDULE_NOT_FOUND -> {
                "스케줄 정보를 찾을 수 없습니다. 다시 시도해주세요"
            }
            else -> {
                null
            }
        }
        codeMessage?.run { showToast(this) }
    }

    private fun copyToClipboard(text: String) {
        AnalyticsManager.addEvent(eventName = LOG_EVENT_LIST_DETAIL_COPY)
        (getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager)?.let { clipboardManager ->
            val clip = ClipData.newPlainText("location", text)
            clipboardManager.setPrimaryClip(clip)
        }
    }

    private fun moveToPlatformAttendance() {
        val intent = PlatformAttendanceActivity.newIntent(this, viewModel.scheduleId)
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context, scheduleId: Int, scheduleType: String) =
            Intent(context, ScheduleDetailActivity::class.java).apply {
                putExtra(EXTRA_SCHEDULE_ID, scheduleId)
                putExtra(EXTRA_SCHEDULE_TYPE, scheduleType)
            }
    }
}
