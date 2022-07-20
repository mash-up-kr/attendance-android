package com.mashup.ui.model


data class AttendanceModel(
    val id: Int,
    val type: Int,
    val profile: Profile?
) {
    companion object {
        val EMPTY = AttendanceModel(
            id = 0,
            type = 0,
            profile = Profile.EMPTY
        )
    }
}

data class Profile(
    val platform: Platform,
    val name: String,
    val score: Int,
) {
    companion object {
        val EMPTY = Profile(
            platform = Platform.NODE,
            name = "test",
            score = 2
        )
    }

    fun getAttendanceScore() = "${score}점"
}