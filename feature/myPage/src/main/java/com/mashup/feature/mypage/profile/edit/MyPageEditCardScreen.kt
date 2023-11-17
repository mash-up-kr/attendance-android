package com.mashup.feature.mypage.profile.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.model.Platform
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.mypage.profile.MyPageEditCellDivider
import com.mashup.feature.mypage.profile.MyPageEditReadOnlyCell
import com.mashup.feature.mypage.profile.MyPageEditWriteCell
import com.mashup.feature.mypage.profile.MyPageProfileEditViewModel

@Composable
fun MyPageEditCardScreen(
    viewModel: MyPageProfileEditViewModel,
    onBackPressed: () -> Unit
) {
    val editCardState by viewModel.myProfileCard.collectAsState()

    MyPageEditCardContent(
        generationNumber = editCardState.generationNumber,
        platform = editCardState.platform,
        onSaveButtonClicked = { id, team, staff ->
            viewModel.patchMemberProfileCard(
                id = id.toLong(),
                projectTeamName = team,
                staff = staff
            )
        },
        onBackPressed = onBackPressed,
        id = editCardState.id,
        team = editCardState.team,
        staff = editCardState.staff
    )
}

@Composable
fun MyPageEditCardContent(
    generationNumber: Int,
    platform: Platform,
    onSaveButtonClicked: (id: Int, team: String, staff: String) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    id: Int = -1,
    team: String = "",
    staff: String = ""
) {
    var teamState by remember(team) { mutableStateOf(team) }
    var staffState by remember(staff) { mutableStateOf(staff) }

    Column(
        modifier = modifier
    ) {
        MashUpToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = "활동 카드 편집",
            showBackButton = true,
            onClickBackButton = { onBackPressed() }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(15.dp))
                MyPageEditCellDivider()
                MyPageEditReadOnlyCell(title = "기수", value = "${generationNumber}기")
                MyPageEditCellDivider()
                MyPageEditReadOnlyCell(title = "플랫폼", value = platform.detailName)
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "프로젝트 팀",
                    value = teamState,
                    hint = "프로젝트 팀을 입력해주세요",
                    onValueChanged = { if (it.length <= 15) teamState = it }
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "스태프",
                    value = staffState,
                    hint = "스태프를 입력해주세요",
                    onValueChanged = { if (it.length <= 15) staffState = it }
                )
                MyPageEditCellDivider()
            }
            MashUpButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                text = "저장",
                buttonStyle = ButtonStyle.PRIMARY,
                isEnabled = teamState != team || staff != staffState,
                onClick = {
                    onSaveButtonClicked(id, teamState, staffState)
                }
            )
        }
    }
}

@Preview
@Composable
fun MyPageEditContentPrev() {
    MashUpTheme {
        Surface(
            color = Gray50
        ) {
            MyPageEditCardContent(
                modifier = Modifier.fillMaxSize(),
                generationNumber = 12,
                platform = Platform.DESIGN,
                onSaveButtonClicked = { _, _, _ -> },
                onBackPressed = {}
            )
        }
    }
}
