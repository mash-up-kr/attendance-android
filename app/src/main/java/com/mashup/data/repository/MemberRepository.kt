package com.mashup.data.repository

import com.mashup.core.model.Platform
import com.mashup.data.dto.AccessResponse
import com.mashup.data.dto.LoginRequest
import com.mashup.data.dto.MemberInfoResponse
import com.mashup.data.dto.SignUpRequest
import com.mashup.data.dto.TokenResponse
import com.mashup.data.dto.ValidResponse
import com.mashup.network.Response
import com.mashup.network.dao.MemberDao
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val memberDao: MemberDao
) {
    suspend fun login(
        identification: String,
        password: String
    ): Response<AccessResponse> {
        return memberDao.postLogin(
            LoginRequest(
                identification = identification,
                password = password
            )
        )
    }

    suspend fun refreshToken(): Response<TokenResponse> {
        return memberDao.postToken()
    }

    suspend fun signup(
        identification: String,
        inviteCode: String,
        name: String,
        password: String,
        platform: String,
        privatePolicyAgreed: Boolean
    ): Response<AccessResponse> {
        return memberDao.postSignUp(
            SignUpRequest(
                identification = identification,
                inviteCode = inviteCode,
                name = name,
                password = password,
                platform = Platform.getPlatform(platform),
                privatePolicyAgreed = privatePolicyAgreed
            )
        )
    }

    suspend fun validateSignUpCode(
        inviteCode: String,
        platform: String
    ): Response<ValidResponse> {
        return memberDao.getValidateSignUpCode(
            inviteCode = inviteCode,
            platform = Platform.getPlatform(platform)
        )
    }

    suspend fun validateId(
        id: String
    ): Response<ValidResponse> {
        return memberDao.getValidId(
            id = id
        )
    }

    suspend fun getMember(): Response<MemberInfoResponse> {
        return memberDao.getMember()
    }

    suspend fun deleteMember(): Response<Any> {
        return memberDao.deleteMember()
    }
}
