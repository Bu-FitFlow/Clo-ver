package com.fitflow.clover.presentation.diagnosis

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.fitflow.clover.core.network.NetworkModule
import com.fitflow.clover.data.repository.DiagnosisRepositoryImpl
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.HttpException
import java.io.IOException

@Composable
fun rememberDiagnosisViewModel(
    memberId: Long?,
    displayName: String?,
    gender: String? = null
): DiagnosisViewModel {
    val context = LocalContext.current

    val networkModule = remember(context) {
        NetworkModule(context)
    }

    val viewModel = remember(context) {
        DiagnosisViewModel(
            diagnosisRepository = DiagnosisRepositoryImpl(
                diagnosisApi = networkModule.diagnosisApi
            )
        )
    }

    LaunchedEffect(memberId, displayName, gender) {
        viewModel.setMemberProfile(
            memberId = memberId,
            displayName = displayName,
            gender = gender
        )

        if (memberId == null || memberId <= 0L) {
            runCatching {
                val rawResponse = networkModule.userApi.getMyInfo()
                val payload = rawResponse.payloadObject()

                val currentMemberId = payload.longOrNull(
                    "memberId",
                    "member_id",
                    "id",
                    "userId",
                    "user_id"
                )

                val currentDisplayName = displayName
                    ?: payload.stringOrNull("nickname")
                    ?: payload.stringOrNull("name")
                    ?: payload.stringOrNull("loginId", "login_id")
                    ?: payload.stringOrNull("username")

                val currentGender = gender
                    ?: payload.stringOrNull("gender", "sex")

                Log.d(
                    TAG,
                    "회원 정보 복구 성공: memberId=$currentMemberId, displayName=$currentDisplayName, gender=$currentGender, raw=$rawResponse"
                )

                viewModel.setMemberProfile(
                    memberId = currentMemberId,
                    displayName = currentDisplayName,
                    gender = currentGender
                )
            }.onFailure { error ->
                val message = when (error) {
                    is HttpException -> {
                        val errorBody = error.response()
                            ?.errorBody()
                            ?.string()

                        "HTTP ${error.code()}, body=$errorBody"
                    }

                    is IOException -> {
                        "네트워크 연결 실패: ${error.message}"
                    }

                    else -> {
                        error.message ?: "알 수 없는 오류"
                    }
                }

                Log.e(TAG, "회원 정보 복구 실패: $message", error)
            }
        }
    }

    return viewModel
}

private fun JsonObject.payloadObject(): JsonObject {
    val wrapperKeys = listOf(
        "data",
        "payload",
        "result",
        "body"
    )

    for (key in wrapperKeys) {
        val value = get(key)

        if (value != null && !value.isJsonNull && value.isJsonObject) {
            val wrapperObject = value.asJsonObject

            val nestedMember = wrapperObject.objectOrNull("member")
                ?: wrapperObject.objectOrNull("user")
                ?: wrapperObject.objectOrNull("memberInfo")
                ?: wrapperObject.objectOrNull("userInfo")

            return nestedMember ?: wrapperObject
        }
    }

    val nestedMember = objectOrNull("member")
        ?: objectOrNull("user")
        ?: objectOrNull("memberInfo")
        ?: objectOrNull("userInfo")

    return nestedMember ?: this
}

private fun JsonObject.objectOrNull(key: String): JsonObject? {
    val value = get(key)

    return if (value != null && !value.isJsonNull && value.isJsonObject) {
        value.asJsonObject
    } else {
        null
    }
}

private fun JsonObject.stringOrNull(vararg keys: String): String? {
    for (key in keys) {
        val value = get(key).asStringOrNull()

        if (!value.isNullOrBlank()) {
            return value
        }
    }

    return null
}

private fun JsonObject.longOrNull(vararg keys: String): Long? {
    for (key in keys) {
        val value = get(key) ?: continue

        if (value.isJsonNull) {
            continue
        }

        runCatching {
            value.asLong
        }.getOrNull()?.let {
            return it
        }

        value.asStringOrNull()
            ?.toLongOrNull()
            ?.let {
                return it
            }
    }

    return null
}

private fun JsonElement?.asStringOrNull(): String? {
    if (this == null || isJsonNull) {
        return null
    }

    return runCatching {
        asString
    }.getOrNull()
}

private const val TAG = "DiagnosisProfile"