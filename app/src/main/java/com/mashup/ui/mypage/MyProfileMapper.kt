package com.mashup.ui.mypage

import com.mashup.data.model.ScoreDetails
import com.mashup.feature.mypage.profile.data.dto.MemberGenerationsResponse
import com.mashup.feature.mypage.profile.data.dto.MemberProfileResponse
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.feature.mypage.profile.model.ProfileData
import com.mashup.ui.model.ActivityHistory
import javax.inject.Inject

class MyProfileMapper @Inject constructor() {

    fun mapToActivityHistory(response: ScoreDetails) = ActivityHistory(
        scoreName = response.scoreName,
        attendanceType = AttendanceType.getAttendanceType(response.scoreType),
        cumulativeScore = response.cumulativeScore,
        score = response.score,
        detail = response.scheduleName,
        date = response.date
    )

    fun mapToProfileData(response: MemberProfileResponse) = ProfileData(
        birthDay = response.birthDate.orEmpty().trim(),
        work = response.job.orEmpty().trim(),
        company = response.company.orEmpty().trim(),
        introduceMySelf = response.introduction.orEmpty().trim(),
        location = response.residence.orEmpty().trim(),
        instagram = response.socialNetworkServiceLink.orEmpty().trim(),
        github = response.githubLink.orEmpty().trim(),
        behance = response.portfolioLink.orEmpty().trim(),
        linkedIn = response.linkedInLink.orEmpty().trim(),
        tistory = response.blogLink.orEmpty().trim()
    )
    fun mapToProfileCardData(
        name: String,
        response: MemberGenerationsResponse.MemberGeneration
    ) = ProfileCardData(
        id = response.id,
        name = name,
        isRunning = response.status != "DONE",
        generationNumber = response.number,
        platform = response.platform,
        projectTeamName = response.projectTeamName.orEmpty(),
        role = response.role.orEmpty()
    )
}
