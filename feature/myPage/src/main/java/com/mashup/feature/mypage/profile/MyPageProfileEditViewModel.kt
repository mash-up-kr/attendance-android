package com.mashup.feature.mypage.profile

import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.model.Platform
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.mypage.profile.data.MyProfileRepository
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
    private val userPreferenceRepository: UserPreferenceRepository,
    private val myProfileRepository: MyProfileRepository
) : BaseViewModel() {

    init {
        getMemberProfileCard()
        getMemberGenerationList()
    }

    private val _myProfileCard = MutableStateFlow(MyProfileCardEntity())
    val myProfileCard = _myProfileCard.asStateFlow()

    private val _myPageCardEntity = MutableStateFlow(ProfileData())
    val myPageCardEntity = _myPageCardEntity.asStateFlow()

    private val _loadState: MutableStateFlow<LoadState> = MutableStateFlow(LoadState.Initial)
    val loadState = _loadState.asStateFlow()

    private val _invalidInputError = MutableSharedFlow<String>()
    val invalidInputError: SharedFlow<String> = _invalidInputError.asSharedFlow()

    private fun getMemberProfileCard() = mashUpScope {
        // 진행중인 활동 카드라서 0번째꺼 뽑아씀
        val teamAndStaff = myProfileRepository.getMemberGenerations()
        _myProfileCard.value = MyProfileCardEntity(
            id = teamAndStaff.data?.memberGenerations?.getOrNull(0)?.id ?: -1,
            generationNumber = teamAndStaff.data?.memberGenerations?.getOrNull(0)?.number ?: userPreferenceRepository.getCurrentGenerationNumber(),
            platform = Platform.getPlatform(teamAndStaff.data?.memberGenerations?.getOrNull(0)?.platform),
            team = teamAndStaff.data?.memberGenerations?.getOrNull(0)?.projectTeamName.orEmpty(),
            staff = teamAndStaff.data?.memberGenerations?.getOrNull(0)?.role.orEmpty()
        )
    }

    fun patchMemberProfileCard(id: Long, projectTeamName: String, staff: String) = mashUpScope {
        _loadState.emit(LoadState.Loading)
        val result = myProfileRepository.postMemberGenerations(id, projectTeamName, staff)
        if (result.isSuccess()) {
            _myProfileCard.value = myProfileCard.value.copy(
                team = projectTeamName,
                staff = staff
            )
        }
        _loadState.emit(LoadState.Loaded)
    }

    private fun getMemberGenerationList() = mashUpScope {
        val profile = myProfileRepository.getMyProfile().data
        _myPageCardEntity.value = profile?.run {
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
            _loadState.emit(LoadState.Loading)
            myProfileRepository.postMyProfile(editedProfileData)
            _loadState.emit(LoadState.Loaded)
        } else {
            _invalidInputError.emit("생년월일 8자리를 입력해주세요")
        }
    }

    private fun checkBirthDay(birthDay: String): Boolean {
        return birthDay.matches(Regex("^[0-9]{8}")) || birthDay.matches(Regex("^[0-9]{4}-[0-9]{2}-[0-9]{2}"))
    }
}

sealed class LoadState {
    object Initial : LoadState()
    object Loading : LoadState()
    object Loaded : LoadState()
}

data class MyProfileCardEntity(
    val id: Int = 0,
    val generationNumber: Int = 0,
    val platform: Platform = Platform.UNKNOWN,
    val team: String = "",
    val staff: String = ""
)
