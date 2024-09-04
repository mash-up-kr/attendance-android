package com.mashup.ui.mypage

import androidx.annotation.DrawableRes
import com.mashup.core.common.R

enum class AttendanceType(@DrawableRes val resourceId: Int) {
    ETC(R.drawable.img_placeholder_history),
    SEMINAR_PRESENTATION(R.drawable.img_presentation),
    DEPLOY_FAILURE(R.drawable.img_projectfail),
    DEPLOY_SUCCESS(R.drawable.img_projectsuccess),
    ATTENDANCE(R.drawable.img_attendance),
    ABSENT(R.drawable.img_absent),
    LATE(R.drawable.img_late),
    HACKATHON_COMMITTEE(R.drawable.img_hackathonprepare),
    PROJECT_LEADER(R.drawable.img_projectleader),
    PROJECT_SUBLEADER(R.drawable.img_projectsubleader),
    MASHUP_STAFF(R.drawable.img_staff),
    MASHUP_LEADER(R.drawable.img_mashupleader),
    MASHUP_SUBLEADER(R.drawable.img_mashupsubleader),
    TECH_BLOG_WRITE(R.drawable.img_techblogwrite),
    MASHUP_CONTENTS_WRITE(R.drawable.img_mashupcontentswrite),
    ADD_SCORE_DURING_SEMINAR_ACTIVITY_0_5(R.drawable.img_presentation),
    ADD_SCORE_DURING_SEMINAR_ACTIVITY_1(R.drawable.img_presentation),
    DEFAULT(R.drawable.img_default_score);

    companion object {
        fun getAttendanceType(scoreType: String?): AttendanceType {
            return values().find { it.name == scoreType?.uppercase() }
                ?: ETC
        }
    }
}
