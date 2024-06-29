package com.mashup.feature.mypage.profile.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.mypage.profile.MyPageEditCellDivider
import com.mashup.feature.mypage.profile.MyPageEditWriteCell
import com.mashup.feature.mypage.profile.MyPageProfileEditViewModel
import com.mashup.feature.mypage.profile.model.LoadState
import com.mashup.feature.mypage.profile.model.ProfileData

@Composable
fun MyPageEditProfileScreen(
    viewModel: MyPageProfileEditViewModel,
    onBackPressed: () -> Unit
) {
    val myProfile by viewModel.myProfileState.collectAsState()
    val isLoading by viewModel.profileCardState.collectAsState()
    MyPageEditProfileContent(
        onSaveButtonClicked = { editedProfile ->
            viewModel.patchMyProfile(
                editedProfile
            )
        },
        onBackPressed = onBackPressed,
        modifier = Modifier,
        isUploading = when (isLoading) {
            LoadState.Initial,
            LoadState.Loaded -> false
            LoadState.Loading -> true
        },
        birthDay = myProfile.birthDay,
        work = myProfile.work,
        company = myProfile.company,
        introduceMySelf = myProfile.introduceMySelf,
        location = myProfile.location,
        instagram = myProfile.instagram,
        github = myProfile.github,
        behance = myProfile.behance,
        linkedIn = myProfile.linkedIn,
        tistory = myProfile.tistory
    )
}

@Composable
fun MyPageEditProfileContent(
    onSaveButtonClicked: (ProfileData) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    isUploading: Boolean = false,
    birthDay: String = "",
    work: String = "",
    company: String = "",
    introduceMySelf: String = "",
    location: String = "",
    instagram: String = "",
    github: String = "",
    behance: String = "",
    linkedIn: String = "",
    tistory: String = ""
) {
    var birthDayState by rememberSaveable(birthDay) { mutableStateOf(birthDay) }
    var workState by rememberSaveable(work) { mutableStateOf(work) }
    var companyState by rememberSaveable(company) { mutableStateOf(company) }
    var introduceMySelfState by rememberSaveable(introduceMySelf) { mutableStateOf(introduceMySelf) }
    var locationState by rememberSaveable(location) { mutableStateOf(location) }
    var instagramState by rememberSaveable(instagram) { mutableStateOf(instagram) }
    var githubState by rememberSaveable(github) { mutableStateOf(github) }
    var behanceState by rememberSaveable(behance) { mutableStateOf(behance) }
    var linkedInState by rememberSaveable(linkedIn) { mutableStateOf(linkedIn) }
    var tistoryState by rememberSaveable(tistory) { mutableStateOf(tistory) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MashUpToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = "내 소개 편집",
            showBackButton = true,
            onClickBackButton = { onBackPressed() }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                MyPageEditWriteCell(
                    title = "생년월일",
                    value = birthDayState,
                    hint = "생년월일 8자리를 추가해주세요",
                    onValueChanged = {
                        // 숫자 8자리 체크
                        val input = it.filter { char -> char.isDigit() }
                        if (input.length <= 8) birthDayState = input
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "직군",
                    value = workState,
                    hint = "현재 직군을 추가해주세요",
                    onValueChanged = {
                        if (it.length <= 25) workState = it
                    }
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "회사",
                    value = companyState,
                    hint = "소속된 회사를 추가해주세요",
                    onValueChanged = {
                        if (it.length <= 25) companyState = it
                    }
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "자기소개",
                    value = introduceMySelfState,
                    hint = "내용을 추가해주세요",
                    onValueChanged = { introduceMySelfState = it }
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "출몰지역",
                    value = locationState,
                    hint = "자주 돌아다니는 지역을 추가해주세요",
                    onValueChanged = {
                        if (it.length <= 25) locationState = it
                    }
                )
                Spacer(modifier = Modifier.height(40.dp))
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "instagram",
                    value = instagramState,
                    hint = "아이디",
                    onValueChanged = { instagramState = it }
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "github",
                    value = githubState,
                    hint = "링크를 추가해주세요",
                    onValueChanged = { githubState = it }
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "Behance",
                    value = behanceState,
                    hint = "링크를 추가해주세요",
                    onValueChanged = { behanceState = it }
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "LinkedIn",
                    value = linkedInState,
                    hint = "링크를 추가해주세요",
                    onValueChanged = { linkedInState = it }
                )
                MyPageEditCellDivider()
                MyPageEditWriteCell(
                    title = "Tistory",
                    value = tistoryState,
                    hint = "링크를 추가해주세요",
                    onValueChanged = { tistoryState = it }
                )
                MyPageEditCellDivider()
            }
            MashUpButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                text = "저장",
                buttonStyle = ButtonStyle.PRIMARY,
                showLoading = isUploading,
                onClick = {
                    onSaveButtonClicked(
                        ProfileData(
                            birthDay = birthDayState,
                            work = workState,
                            company = companyState,
                            introduceMySelf = introduceMySelfState,
                            location = locationState,
                            instagram = instagramState,
                            github = githubState,
                            behance = behanceState,
                            linkedIn = linkedInState,
                            tistory = tistoryState
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun MyPageEditProfileContentPrev() {
    MashUpTheme {
        Surface(
            color = Gray50
        ) {
            MyPageEditProfileContent(
                modifier = Modifier.fillMaxSize(),
                onSaveButtonClicked = {
                },
                onBackPressed = {
                }
            )
        }
    }
}
