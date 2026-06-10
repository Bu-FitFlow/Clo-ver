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
            ),
            memberProfileLoader = {
                networkModule.userApi
                    .getMyInfo()
                    .toDiagnosisMemberProfile()
            }
        )
    }

    LaunchedEffect(memberId, displayName, gender) {
        viewModel.setMemberProfile(
            memberId = memberId,
            displayName = displayName,
            gender = gender
        )

        if (memberId == null || memberId <= 0L) {
            val loaded = viewModel.refreshMemberProfileFromServer()

            Log.d(
                TAG,
                "초기 회원 정보 복구 결과: loaded=$loaded"
            )
        }
    }

    return viewModel
}

private fun JsonObject.toDiagnosisMemberProfile(): DiagnosisMemberProfile {
    val currentMemberId = findLongRecursively(
        "memberId",
        "member_id",
        "id",
        "userId",
        "user_id"
    )

    val currentDisplayName = findStringRecursively(
        "nickname",
        "name",
        "loginId",
        "login_id",
        "username"
    )

    val currentGender = findStringRecursively(
        "gender",
        "sex"
    )

    Log.d(
        TAG,
        "회원 정보 응답 파싱 결과: memberId=$currentMemberId, displayName=$currentDisplayName, gender=$currentGender, raw=$this"
    )

    return DiagnosisMemberProfile(
        memberId = currentMemberId,
        displayName = currentDisplayName,
        gender = currentGender
    )
}

private fun JsonObject.findLongRecursively(
    vararg keys: String
): Long? {
    for (key in keys) {
        val parsed = get(key).asLongOrNull()
        if (parsed != null) {
            return parsed
        }
    }

    for ((_, value) in entrySet()) {
        if (value != null && !value.isJsonNull && value.isJsonObject) {
            val parsed = value.asJsonObject.findLongRecursively(*keys)
            if (parsed != null) {
                return parsed
            }
        }
    }

    return null
}

private fun JsonObject.findStringRecursively(
    vararg keys: String
): String? {
    for (key in keys) {
        val parsed = get(key).asStringOrNull()
        if (!parsed.isNullOrBlank()) {
            return parsed
        }
    }

    for ((_, value) in entrySet()) {
        if (value != null && !value.isJsonNull && value.isJsonObject) {
            val parsed = value.asJsonObject.findStringRecursively(*keys)
            if (!parsed.isNullOrBlank()) {
                return parsed
            }
        }
    }

    return null
}

private fun JsonElement?.asLongOrNull(): Long? {
    if (this == null || isJsonNull) {
        return null
    }

    runCatching {
        asLong
    }.getOrNull()?.let {
        return it
    }

    return asStringOrNull()?.toLongOrNull()
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