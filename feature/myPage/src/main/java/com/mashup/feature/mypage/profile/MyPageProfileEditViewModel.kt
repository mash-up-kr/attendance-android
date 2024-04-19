package com.mashup.feature.mypage.profile

import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.model.Platform
import com.mashup.feature.mypage.profile.data.MyProfileRepository
import com.mashup.feature.mypage.profile.model.LoadState
import com.mashup.feature.mypage.profile.model.MyProfileCardEntity
import com.mashup.feature.mypage.profile.model.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageProfileEditViewModel @Inject constructor(
    private val myProfileRepository: MyProfileRepository
) : BaseViewModel() {

    init {
        getMemberProfileCard()
        getMemberGenerationList()
    }

    private val _myProfileState = MutableStateFlow(ProfileData())
    val myProfileState = _myProfileState.asStateFlow()

    private var profileCardList = emptyList<MyProfileCardEntity>()

    private val _profileCardState: MutableStateFlow<LoadState> = MutableStateFlow(LoadState.Initial)
    val profileCardState = _profileCardState.asStateFlow()

    private val _invalidInputError = MutableSharedFlow<String>()
    val invalidInputError: SharedFlow<String> = _invalidInputError.asSharedFlow()

    private fun getMemberProfileCard() = mashUpScope {
        val teamAndStaff = myProfileRepository.getMemberGenerations()
        profileCardList = teamAndStaff.data?.memberGenerations?.map { member ->
            MyProfileCardEntity(
                id = member.id,
                generationNumber = member.number,
                platform = Platform.getPlatform(member.platform),
                team = member.projectTeamName.orEmpty(),
                staff = member.role.orEmpty()
            )
        } ?: emptyList()
    }

    fun patchMemberProfileCard(id: Long, projectTeamName: String, staff: String) = mashUpScope {
        _profileCardState.emit(LoadState.Loading)
        val result = myProfileRepository.postMemberGenerations(id, projectTeamName, staff)
        if (result.isSuccess()) {
            profileCardList = profileCardList.map {
                if (it.id.toLong() == id) {
                    it.copy(
                        team = projectTeamName,
                        staff = staff
                    )
                } else {
                    it
                }
            }
        }
        _profileCardState.emit(LoadState.Loaded)
    }

    private fun getMemberGenerationList() = mashUpScope {
        val profile = myProfileRepository.getMyProfile().data
        _myProfileState.value = profile?.run {
            ProfileData(
                birthDay = birthDate.orEmpty(),
                work = job.orEmpty(),
                company = company.orEmpty(),
                introduceMySelf = introduction.orEmpty(),
                location = residence.orEmpty(),
                instagram = socialNetworkServiceLink.orEmpty(),
                github = githubLink.orEmpty(),
                behance = portfolioLink.orEmpty(),
                linkedIn = linkedInLink.orEmpty(),
                tistory = blogLink.orEmpty()
            )
        } ?: ProfileData()
    }

    fun patchMyProfile(
        editedProfileData: ProfileData
    ) = mashUpScope {
        if (checkBirthDay(editedProfileData.birthDay)) {
            _profileCardState.emit(LoadState.Loading)
            myProfileRepository.postMyProfile(editedProfileData)
            _profileCardState.emit(LoadState.Loaded)
        } else {
            _invalidInputError.emit("생년월일 8자리를 입력해주세요")
        }
    }

    private fun checkBirthDay(birthDay: String): Boolean {
        return birthDay.matches(Regex("^[0-9]{8}")) || birthDay.matches(Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}"))
    }
}
