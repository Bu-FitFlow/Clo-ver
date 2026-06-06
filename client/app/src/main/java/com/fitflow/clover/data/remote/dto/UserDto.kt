package com.fitflow.clover.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MemberUpdateRequest(
    @SerializedName("nickname")
    val nickname: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("gender")
    val gender: String? = null
)
