package com.fitflow.clover.core.network

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName(value = "timestamp")
    val timestamp: String? = null,

    @SerializedName(value = "status")
    val status: Int? = null,

    @SerializedName(value = "error")
    val error: String? = null,

    @SerializedName(value = "message")
    val message: String? = null,

    @SerializedName(value = "path")
    val path: String? = null
) {
    fun getDisplayMessage(): String {
        return message ?: error ?: "서버 요청 중 오류가 발생했습니다."
    }
}
