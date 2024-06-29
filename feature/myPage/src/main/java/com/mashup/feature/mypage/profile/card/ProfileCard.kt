package com.mashup.feature.mypage.profile.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.common.R
import com.mashup.core.model.Platform
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3
import com.mashup.core.ui.typography.Caption1
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.feature.mypage.profile.model.ProfileCardData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCard(
    cardData: ProfileCardData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() }
            .background(
                color = colorResource(getPlatformBackgroundColor(cardData.platform)),
                RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(top = 12.dp, start = 12.dp),
                text = "${cardData.generationNumber}기",
                style = Body3,
                color = Color.White.copy(alpha = 0.5f)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .size(140.dp, 114.dp),
                    painter = painterResource(getPlatformImage(cardData.platform)),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = cardData.name,
                    style = SubTitle1,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                modifier = Modifier.padding(top = 12.dp, end = 12.dp),
                text = if (cardData.isRunning) "활동 중" else "완료",
                style = Body3,
                color = Color.White.copy(alpha = 0.5f)
            )
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ProfileCardTag(
                text = cardData.platform.detailName,
                backgroundColor = colorResource(getPlatformTagBackgroundColor(cardData.platform)),
                textColor = colorResource(getPlatformTagTextColor(cardData.platform))
            )
            if (cardData.projectTeamName.isNotBlank()) {
                ProfileCardTag(
                    text = cardData.projectTeamName,
                    backgroundColor = colorResource(getPlatformTagBackgroundColor(cardData.platform)),
                    textColor = colorResource(getPlatformTagTextColor(cardData.platform))
                )
            }
            if (cardData.role.isNotBlank()) {
                ProfileCardTag(
                    text = cardData.role,
                    backgroundColor = colorResource(getPlatformTagBackgroundColor(cardData.platform)),
                    textColor = colorResource(getPlatformTagTextColor(cardData.platform))
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProfileCardTag(
    text: String,
    backgroundColor: Color,
    textColor: Color
) {
    Text(
        modifier = Modifier
            .padding(3.dp)
            .background(backgroundColor, CircleShape)
            .padding(vertical = 5.dp, horizontal = 10.dp),
        text = text,
        style = Caption1,
        color = textColor
    )
}

@Preview
@Composable
fun ProfileCardPrev() {
    MashUpTheme {
        ProfileCard(
            cardData = ProfileCardData(
                id = 0,
                generationNumber = 12,
                name = "김매숑",
                platform = Platform.ANDROID,
                isRunning = true,
                projectTeamName = "",
                role = ""
            ),
            onClick = {}
        )
    }
}

private fun getPlatformImage(platform: Platform): Int {
    return when (platform) {
        Platform.ANDROID -> R.drawable.img_profile_android

        Platform.DESIGN -> R.drawable.img_profile_design

        Platform.IOS -> R.drawable.img_profile_ios

        Platform.WEB -> R.drawable.img_profile_web

        Platform.SPRING -> R.drawable.img_profile_spring

        Platform.NODE -> R.drawable.img_profile_node

        else -> R.drawable.img_profile_design
    }
}

private fun getPlatformBackgroundColor(platform: Platform): Int {
    return when (platform) {
        Platform.ANDROID -> R.color.teamAndroid200

        Platform.DESIGN -> R.color.teamProductDesign200

        Platform.IOS -> R.color.teamIos200

        Platform.WEB -> R.color.teamWeb200

        Platform.SPRING -> R.color.teamSpring200

        Platform.NODE -> R.color.teamNode200

        else -> R.color.teamAndroid200
    }
}

private fun getPlatformTagBackgroundColor(platform: Platform): Int {
    return when (platform) {
        Platform.ANDROID -> R.color.teamAndroid300

        Platform.DESIGN -> R.color.teamProductDesign300

        Platform.IOS -> R.color.teamIos300

        Platform.WEB -> R.color.teamWeb300

        Platform.SPRING -> R.color.teamSpring300

        Platform.NODE -> R.color.teamNode300

        else -> R.color.teamAndroid300
    }
}

private fun getPlatformTagTextColor(platform: Platform): Int {
    return when (platform) {
        Platform.ANDROID -> R.color.teamAndroid100

        Platform.DESIGN -> R.color.teamProductDesign100

        Platform.IOS -> R.color.teamIos100

        Platform.WEB -> R.color.teamWeb100

        Platform.SPRING -> R.color.teamSpring100

        Platform.NODE -> R.color.teamNode100

        else -> R.color.teamAndroid100
    }
}
