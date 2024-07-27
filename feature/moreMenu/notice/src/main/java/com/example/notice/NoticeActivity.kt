package com.example.notice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mashup.core.ui.theme.MashUpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeActivity : ComponentActivity() {
    private val noticeViewModel: NoticeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MashUpTheme {
                NoticeRoute(
                    modifier = Modifier.fillMaxSize(),
                    noticeViewModel = noticeViewModel,
                    onBackPressed = ::finish
                )
            }
        }
    }
}
