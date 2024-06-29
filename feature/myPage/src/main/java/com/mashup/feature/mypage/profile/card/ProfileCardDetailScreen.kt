package com.mashup.feature.mypage.profile.card

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.model.Platform
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.mypage.profile.model.ProfileCardData

@Composable
fun ProfileCardDetailContent(
    cardData: ProfileCardData,
    team: String,
    staff: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onDownLoadClicked: (Bitmap) -> Unit = {},
    onEditClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MashUpToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = "활동 카드",
            showBackButton = true,
            onClickBackButton = onBackPressed
        )

        val snapshot = captureBitmap {
            ProfileCard(
                cardData = cardData.copy(
                    projectTeamName = team,
                    role = staff
                ),
                modifier = Modifier
                    .width(400.dp)
                    .padding(horizontal = 20.dp),
                onClick = {}
            )
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                MashUpButton(
                    modifier = Modifier.weight(1f),
                    text = "다운로드",
                    buttonStyle = ButtonStyle.DARK,
                    onClick = {
                        val bitmap = snapshot.invoke()
                        onDownLoadClicked(bitmap)
                    }
                )

                MashUpButton(
                    modifier = Modifier.weight(1f),
                    text = "편집",
                    buttonStyle = ButtonStyle.PRIMARY,
                    onClick = onEditClicked
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileCardDetailContentPrev() {
    MashUpTheme {
        Surface(
            color = Gray50
        ) {
            ProfileCardDetailContent(
                modifier = Modifier.fillMaxSize(),
                cardData = ProfileCardData(
                    id = 0,
                    name = "김매숑",
                    isRunning = false,
                    generationNumber = 0,
                    platform = Platform.DESIGN,
                    projectTeamName = "",
                    role = ""
                ),
                team = "",
                staff = ""
            )
        }
    }
}
