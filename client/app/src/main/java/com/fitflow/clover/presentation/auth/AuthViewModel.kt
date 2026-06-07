package com.fitflow.clover.presentation.auth

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitflow.clover.core.network.ErrorResponse
import com.fitflow.clover.core.network.NetworkConstants
import com.fitflow.clover.core.network.NetworkModule
import com.fitflow.clover.data.remote.dto.LoginRequest
import com.fitflow.clover.data.remote.dto.SignUpRequest
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel(
    private val networkModule: NetworkModule
) : ViewModel() {

    private val gson = Gson()

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    fun onNameChange(newValue: String) {
        _name.value = newValue
    }

    private val _id = mutableStateOf("")
    val id: State<String> = _id

    fun onIdChange(newValue: String) {
        _id.value = newValue
    }

    private val _pw = mutableStateOf("")
    val pw: State<String> = _pw

    fun onPwChange(newValue: String) {
        _pw.value = newValue
    }

    private val _pwConfirm = mutableStateOf("")
    val pwConfirm: State<String> = _pwConfirm

    fun onPwConfirmChange(newValue: String) {
        _pwConfirm.value = newValue
    }

    private val _currentMemberId = mutableStateOf<Long?>(null)
    val currentMemberId: State<Long?> = _currentMemberId

    private val _currentDisplayName = mutableStateOf<String?>(null)
    val currentDisplayName: State<String?> = _currentDisplayName

    private val _currentGender = mutableStateOf<String?>(null)
    val currentGender: State<String?> = _currentGender

    fun login(
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val loginId = id.value.trim()
        val password = pw.value

        if (loginId.isBlank() || password.isBlank()) {
            onError("아이디와 비밀번호를 입력해 주세요.")
            return
        }

        viewModelScope.launch {
            try {
                val response = networkModule.authApi.login(
                    LoginRequest(
                        loginId = loginId,
                        password = password
                    )
                )

                val accessToken = response.accessToken
                val refreshToken = response.refreshToken

                if (accessToken.isNullOrBlank()) {
                    Log.e(TAG_LOGIN, "로그인 실패: accessToken 없음. response=$response")
                    onError("로그인 응답에서 토큰을 받지 못했어요. 백엔드 응답 구조를 확인해 주세요.")
                    return@launch
                }

                networkModule.tokenDataStore.saveTokens(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )

                val tokenMemberId = accessToken.extractMemberIdFromJwt()

                _currentMemberId.value = response.memberId ?: tokenMemberId
                _currentDisplayName.value = response.nickname
                    ?: response.name
                            ?: response.loginId
                            ?: loginId
                _currentGender.value = response.gender

                Log.d(
                    TAG_LOGIN,
                    "로그인 1차 성공: responseMemberId=${response.memberId}, tokenMemberId=$tokenMemberId, loginId=${response.loginId}, gender=${response.gender}, accessToken=${accessToken.take(20)}..."
                )

                refreshCurrentMemberProfile(
                    fallbackLoginId = loginId,
                    fallbackAccessToken = accessToken
                )

                if (_currentMemberId.value == null) {
                    Log.e(
                        TAG_LOGIN,
                        "로그인은 성공했지만 회원 ID 복구 실패. /api/members/me 응답 또는 JWT payload에 memberId가 필요합니다."
                    )
                }

                Log.d(
                    TAG_LOGIN,
                    "로그인 최종 처리: memberId=${_currentMemberId.value}, displayName=${_currentDisplayName.value}, gender=${_currentGender.value}"
                )

                onSuccess(false)
            } catch (e: Exception) {
                val message = e.toDisplayMessage(
                    defaultMessage = "로그인에 실패했어요. 아이디, 비밀번호, 이메일 인증 상태를 확인해 주세요."
                )

                Log.e(TAG_LOGIN, "로그인 통신 실패: $message", e)
                onError(message)
            }
        }
    }

    fun checkAutoLogin(
        onTokenValid: () -> Unit,
        onTokenInvalid: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val accessToken = networkModule.tokenDataStore.getAccessToken()
                val refreshToken = networkModule.tokenDataStore.getRefreshToken()

                if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                    Log.d(TAG_AUTH, "자동 로그인 실패: 저장된 토큰 없음")
                    onTokenInvalid()
                    return@launch
                }

                val tokenMemberId = accessToken.extractMemberIdFromJwt()
                if (tokenMemberId != null) {
                    _currentMemberId.value = tokenMemberId
                }

                Log.d(
                    TAG_AUTH,
                    "자동 로그인 토큰 발견: tokenMemberId=$tokenMemberId"
                )

                refreshCurrentMemberProfile(
                    fallbackLoginId = "사용자",
                    fallbackAccessToken = accessToken
                )

                Log.d(
                    TAG_AUTH,
                    "자동 로그인 처리: memberId=${_currentMemberId.value}, displayName=${_currentDisplayName.value}, gender=${_currentGender.value}"
                )

                onTokenValid()
            } catch (e: Exception) {
                Log.e(TAG_AUTH, "자동 로그인 체크 실패", e)
                onTokenInvalid()
            }
        }
    }

    private suspend fun refreshCurrentMemberProfile(
        fallbackLoginId: String,
        fallbackAccessToken: String? = null
    ) {
        runCatching {
            val rawResponse = networkModule.userApi.getMyInfo()
            val profile = rawResponse.toMemberProfile()

            Log.d(
                TAG_LOGIN,
                "내 정보 조회 성공 raw=$rawResponse"
            )

            Log.d(
                TAG_LOGIN,
                "내 정보 파싱 결과: memberId=${profile.memberId}, displayName=${profile.displayName}, gender=${profile.gender}"
            )

            _currentMemberId.value = profile.memberId
                ?: _currentMemberId.value
                        ?: fallbackAccessToken.extractMemberIdFromJwt()

            _currentDisplayName.value = profile.displayName
                ?: _currentDisplayName.value
                        ?: fallbackLoginId

            _currentGender.value = profile.gender
                ?: _currentGender.value

            Log.d(
                TAG_LOGIN,
                "내 정보 최종 세팅: memberId=${_currentMemberId.value}, displayName=${_currentDisplayName.value}, gender=${_currentGender.value}"
            )
        }.onFailure { error ->
            Log.e(
                TAG_LOGIN,
                "내 정보 조회 실패: ${error.toDisplayMessage("회원 정보 조회에 실패했어요.")}",
                error
            )

            _currentMemberId.value = _currentMemberId.value
                ?: fallbackAccessToken.extractMemberIdFromJwt()

            _currentDisplayName.value = _currentDisplayName.value
                ?: fallbackLoginId
        }
    }

    fun signUp(
        loginId: String,
        password: String,
        name: String,
        nickname: String,
        email: String,
        gender: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = SignUpRequest(
                    loginId = loginId.trim(),
                    password = password,
                    name = name.trim(),
                    nickname = nickname.trim(),
                    email = email.trim(),
                    gender = gender
                )

                val response = networkModule.authApi.signUp(request)
                Log.d(TAG_SIGN_UP, "회원가입 성공: $response")

                onSuccess()
            } catch (e: Exception) {
                Log.e(
                    TAG_SIGN_UP,
                    "회원가입 통신 실패: ${e.toDisplayMessage("회원가입에 실패했어요.")}",
                    e
                )

                onError()
            }
        }
    }

    private fun JsonObject.toMemberProfile(): MemberProfile {
        val memberId = findLongRecursively(
            "memberId",
            "member_id",
            "id",
            "userId",
            "user_id",
            "memberNo",
            "member_no",
            "userNo",
            "user_no",
            "memberIdx",
            "member_idx"
        )

        val displayName = findStringRecursively(
            "nickname",
            "name",
            "loginId",
            "login_id",
            "username",
            "userName",
            "user_name"
        )

        val gender = findStringRecursively(
            "gender",
            "sex"
        )

        return MemberProfile(
            memberId = memberId,
            displayName = displayName,
            gender = gender
        )
    }

    private fun String?.extractMemberIdFromJwt(): Long? {
        if (isNullOrBlank()) {
            return null
        }

        val token = trim()
            .removePrefix("${NetworkConstants.BEARER_PREFIX} ")
            .removePrefix("${NetworkConstants.BEARER_PREFIX.lowercase()} ")

        val parts = token.split(".")
        if (parts.size < 2) {
            return null
        }

        return runCatching {
            val payload = parts[1]
            val decodedBytes = Base64.decode(
                payload,
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )

            val payloadJson = String(decodedBytes, Charsets.UTF_8)
            val jsonObject = gson.fromJson(payloadJson, JsonObject::class.java)

            Log.d(TAG_LOGIN, "JWT payload=$payloadJson")

            jsonObject.findLongRecursively(
                "memberId",
                "member_id",
                "id",
                "userId",
                "user_id",
                "memberNo",
                "member_no",
                "userNo",
                "user_no",
                "memberIdx",
                "member_idx",
                "sub"
            )
        }.getOrElse { error ->
            Log.e(TAG_LOGIN, "JWT memberId 파싱 실패: ${error.message}", error)
            null
        }
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

    private data class MemberProfile(
        val memberId: Long?,
        val displayName: String?,
        val gender: String?
    )

    private fun Throwable.toDisplayMessage(
        defaultMessage: String
    ): String {
        return when (this) {
            is HttpException -> {
                val statusCode = code()
                val serverMessage = response()
                    ?.errorBody()
                    ?.string()
                    ?.parseServerMessage()

                when {
                    !serverMessage.isNullOrBlank() -> serverMessage
                    statusCode == 400 -> "$defaultMessage 요청값을 확인해 주세요."
                    statusCode == 401 -> "로그인 인증에 실패했어요. 아이디, 비밀번호, 이메일 인증 상태를 확인해 주세요."
                    statusCode == 403 -> "접근 권한이 없어요. 이메일 인증 또는 계정 상태를 확인해 주세요."
                    statusCode == 404 -> "요청한 API 경로를 찾지 못했어요. 백엔드 주소와 API 경로를 확인해 주세요."
                    statusCode == 409 -> "이미 사용 중인 아이디, 이메일 또는 닉네임이에요."
                    statusCode >= 500 -> "서버 오류가 발생했어요. 잠시 후 다시 시도해 주세요."
                    else -> "$defaultMessage (HTTP $statusCode)"
                }
            }

            is IOException -> {
                "서버에 연결하지 못했어요. 인터넷 연결 또는 서버 주소를 확인해 주세요."
            }

            else -> {
                message
                    ?.takeIf { it.isNotBlank() }
                    ?: defaultMessage
            }
        }
    }

    private fun String.parseServerMessage(): String? {
        return runCatching {
            gson.fromJson(this, ErrorResponse::class.java)
                ?.getDisplayMessage()
                ?.takeIf { it.isNotBlank() }
        }.getOrNull()
    }

    companion object {
        private const val TAG_LOGIN = "Login"
        private const val TAG_SIGN_UP = "SignUp"
        private const val TAG_AUTH = "AuthViewModel"
    }
}