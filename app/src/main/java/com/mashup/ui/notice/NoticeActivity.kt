package com.mashup.ui.notice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.notice.NoticeRoute
import com.example.notice.model.NoticeSideEffect
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.login.LoginType
import com.mashup.ui.main.MainActivity
import com.mashup.ui.qrscan.QRScanActivity
import com.mashup.ui.webview.birthday.BirthdayActivity
import com.mashup.ui.webview.mashong.MashongActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeActivity : ComponentActivity() {
    private val noticeViewModel: NoticeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MashUpTheme {
                val noticeState by noticeViewModel.noticeState.collectAsState()

                LaunchedEffect(Unit) {
                    noticeViewModel.noticeEvent.collect {
                        when (it) {
                            is NoticeSideEffect.OnBackPressed -> finish()
                            is NoticeSideEffect.OnNavigateMenu -> {
                                val intent = when (it.notice.linkType) {
                                    "QR" -> {
                                        QRScanActivity.newIntent(this@NoticeActivity)
                                    }

                                    "DANGGN" -> {
                                        ShakeDanggnActivity.newIntent(this@NoticeActivity)
                                    }

                                    "BIRTHDAY" -> {
                                        BirthdayActivity.newIntent(this@NoticeActivity)
                                    }

                                    "MASHONG" -> {
                                        MashongActivity.newIntent(this@NoticeActivity)
                                    }

                                    "SEMINAR" -> {
                                        MainActivity.newIntent(
                                            this@NoticeActivity,
                                            loginType = LoginType.AUTO
                                        )
                                    }

                                    else -> {
                                        MainActivity.newIntent(
                                            this@NoticeActivity,
                                            loginType = LoginType.AUTO
                                        )
                                    }
                                }
                                startActivity(intent)
                            }
                        }
                    }
                }

                NoticeRoute(
                    modifier = Modifier.fillMaxSize(),
                    noticeState = noticeState,
                    onBackPressed = noticeViewModel::onBackPressed,
                    onClickNoticeItem = noticeViewModel::onClickNoticeItem,
                    onLoadNextNotice = noticeViewModel::onLoadNextNotice
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, NoticeActivity::class.java)
    }
}
