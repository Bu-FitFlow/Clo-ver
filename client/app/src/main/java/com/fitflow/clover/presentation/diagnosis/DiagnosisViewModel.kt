package com.fitflow.clover.presentation.diagnosis

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.fitflow.clover.data.repository.DiagnosisRepositoryImpl
import retrofit2.HttpException
import java.io.IOException

data class DiagnosisMemberProfile(
    val memberId: Long?,
    val displayName: String?,
    val gender: String?
)

class DiagnosisViewModel(
    private val diagnosisRepository: DiagnosisRepositoryImpl? = null,
    private val memberProfileLoader: (suspend () -> DiagnosisMemberProfile?)? = null
) {

    var uiState = mutableStateOf(DiagnosisUiState())
        private set

    private var memberId: Long? = null

    val heightOptions: List<Int> = (140..200 step 5).toList()
    val weightOptions: List<Int> = (40..120 step 2).toList()

    fun setMemberProfile(
        memberId: Long?,
        displayName: String?,
        gender: String? = null
    ) {
        val safeMemberId = memberId?.takeIf { it > 0L }

        if (safeMemberId != null) {
            this.memberId = safeMemberId
        }

        setUserDisplayName(displayName)
        setMemberGender(gender)

        Log.d(
            TAG,
            "회원 정보 세팅: inputMemberId=$memberId, savedMemberId=${this.memberId}, displayName=$displayName, gender=$gender"
        )
    }

    suspend fun refreshMemberProfileFromServer(): Boolean {
        return loadMemberProfileFromServer(force = true)
    }

    private suspend fun loadMemberProfileFromServer(
        force: Boolean = false
    ): Boolean {
        if (!force && memberId != null) {
            return true
        }

        val loader = memberProfileLoader

        if (loader == null) {
            Log.e(TAG, "회원 정보 복구 실패: memberProfileLoader 없음")
            return false
        }

        return runCatching {
            Log.d(TAG, "회원 정보 서버 복구 시도")

            val profile = loader()

            if (profile == null) {
                Log.e(TAG, "회원 정보 서버 복구 실패: profile=null")
                return false
            }

            setMemberProfile(
                memberId = profile.memberId,
                displayName = profile.displayName,
                gender = profile.gender
            )

            val loaded = memberId != null

            Log.d(
                TAG,
                "회원 정보 서버 복구 완료: loaded=$loaded, memberId=$memberId"
            )

            loaded
        }.getOrElse { error ->
            Log.e(
                TAG,
                "회원 정보 서버 복구 실패: ${error.toDiagnosisLogMessage()}",
                error
            )
            false
        }
    }

    fun setMemberGender(gender: String?) {
        val parsedGender = gender.toDiagnosisGenderOrNull() ?: return

        uiState.value = uiState.value.copy(
            selectedGender = parsedGender
        )
    }

    private fun String?.toDiagnosisGenderOrNull(): DiagnosisGender? {
        return when (
            this
                ?.trim()
                ?.uppercase()
        ) {
            "MALE", "M", "MAN", "남성", "남자" -> DiagnosisGender.MALE
            "FEMALE", "F", "WOMAN", "여성", "여자" -> DiagnosisGender.FEMALE
            else -> null
        }
    }

    fun setUserDisplayName(displayName: String?) {
        val safeDisplayName = displayName
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: uiState.value.userDisplayName
                .trim()
                .takeIf { it.isNotBlank() }
            ?: "사용자"

        uiState.value = uiState.value.copy(
            userDisplayName = safeDisplayName
        )
    }

    fun selectGender(gender: DiagnosisGender) {
        uiState.value = uiState.value.copy(
            selectedGender = gender
        )

        Log.d(TAG, "성별 선택: gender=${gender.name}")
    }

    fun selectHeight(heightCm: Int) {
        uiState.value = uiState.value.copy(
            selectedHeightCm = heightCm
        )

        Log.d(TAG, "키 선택: height=$heightCm")
    }

    fun selectWeight(weightKg: Int) {
        uiState.value = uiState.value.copy(
            selectedWeightKg = weightKg
        )

        Log.d(TAG, "몸무게 선택: weight=$weightKg")
    }

    fun onFrontBodyPhotoCaptured(bitmap: Bitmap?) {
        if (bitmap == null) {
            uiState.value = uiState.value.copy(
                frontBodyPhotoBitmap = null,
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = "전면 사진을 가져오지 못했어요. 다시 촬영해 주세요."
            )
            return
        }

        uiState.value = uiState.value.copy(
            frontBodyPhotoBitmap = bitmap,
            sideBodyPhotoBitmap = null,
            isBodyAnalyzing = false,
            bodyResult = null,
            bodyAnalysisErrorMessage = null
        )
    }

    fun onSideBodyPhotoCaptured(bitmap: Bitmap?) {
        if (bitmap == null) {
            uiState.value = uiState.value.copy(
                sideBodyPhotoBitmap = null,
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = "옆면 사진을 가져오지 못했어요. 다시 촬영해 주세요."
            )
            return
        }

        uiState.value = uiState.value.copy(
            sideBodyPhotoBitmap = bitmap,
            isBodyAnalyzing = true,
            bodyResult = null,
            bodyAnalysisErrorMessage = null
        )
    }

    fun onBodyPhotoCaptured(bitmap: Bitmap?) {
        onFrontBodyPhotoCaptured(bitmap)
    }

    suspend fun completeBodyAnalysis(): Boolean {
        var state = uiState.value
        val frontBitmap = state.frontBodyPhotoBitmap
        val sideBitmap = state.sideBodyPhotoBitmap

        if (!state.isBodyAnalyzing || frontBitmap == null || sideBitmap == null) {
            uiState.value = state.copy(
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = "전면 사진과 옆면 사진이 모두 필요해요. 다시 촬영해 주세요."
            )
            return false
        }

        if (!isRecognizablePhoto(frontBitmap) || !isRecognizablePhoto(sideBitmap)) {
            uiState.value = state.copy(
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = "체형을 인식할 수 없어요.\n전면과 옆면 전신이 화면 중앙에 보이도록 다시 촬영해 주세요."
            )
            return false
        }

        if (memberId == null) {
            loadMemberProfileFromServer(force = true)
        }

        state = uiState.value

        val safeMemberId = memberId
        if (safeMemberId == null) {
            uiState.value = state.copy(
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = "회원 정보를 불러오지 못했어요. 로그아웃 후 다시 로그인한 뒤 진행해 주세요."
            )
            return false
        }

        val gender = state.selectedGender
        if (gender == null) {
            uiState.value = state.copy(
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = "성별을 선택해 주세요."
            )
            return false
        }

        val heightCm = state.selectedHeightCm
        val weightKg = state.selectedWeightKg
        if (heightCm == null || weightKg == null) {
            uiState.value = state.copy(
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = "키와 몸무게를 입력해 주세요."
            )
            return false
        }

        val repository = diagnosisRepository
        if (repository == null) {
            uiState.value = state.copy(
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = "진단 API 연결 정보가 없습니다."
            )
            return false
        }

        Log.d(
            TAG,
            "체형 분석 요청 시작: memberId=$safeMemberId, gender=${gender.name}, height=$heightCm, weight=$weightKg"
        )

        val apiResult = runCatching {
            repository.analyzeBody(
                memberId = safeMemberId,
                frontBitmap = frontBitmap,
                sideBitmap = sideBitmap,
                gender = gender,
                heightCm = heightCm,
                weightKg = weightKg,
                userDisplayName = state.userDisplayName
            )
        }.getOrElse { error ->
            Log.e(
                TAG,
                "체형 분석 요청 실패: ${error.toDiagnosisLogMessage()}",
                error
            )

            uiState.value = state.copy(
                isBodyAnalyzing = false,
                bodyResult = null,
                bodyAnalysisErrorMessage = error.toAnalysisErrorMessage(
                    defaultMessage = "체형 분석 서버 연결에 실패했어요. 잠시 후 다시 시도해 주세요."
                )
            )
            return false
        }

        uiState.value = state.copy(
            isBodyAnalyzing = false,
            bodyResult = apiResult,
            bodyAnalysisErrorMessage = null
        )

        Log.d(TAG, "체형 분석 요청 성공")

        return true
    }

    fun resetBodyPhoto() {
        resetBodyPhotos()
    }

    fun resetBodyPhotos() {
        uiState.value = uiState.value.copy(
            frontBodyPhotoBitmap = null,
            sideBodyPhotoBitmap = null,
            isBodyAnalyzing = false,
            bodyResult = null,
            bodyAnalysisErrorMessage = null
        )
    }

    fun resetSideBodyPhoto() {
        uiState.value = uiState.value.copy(
            sideBodyPhotoBitmap = null,
            isBodyAnalyzing = false,
            bodyResult = null,
            bodyAnalysisErrorMessage = null
        )
    }

    fun onPersonalColorPhotoCaptured(bitmap: Bitmap?) {
        if (bitmap == null) {
            uiState.value = uiState.value.copy(
                personalColorPhotoBitmap = null,
                isPersonalColorAnalyzing = false,
                personalColorResult = null,
                personalColorAnalysisErrorMessage = "사진을 가져오지 못했어요. 다시 촬영해 주세요."
            )
            return
        }

        uiState.value = uiState.value.copy(
            personalColorPhotoBitmap = bitmap,
            isPersonalColorAnalyzing = true,
            personalColorResult = null,
            personalColorAnalysisErrorMessage = null
        )
    }

    suspend fun completePersonalColorAnalysis(): Boolean {
        var state = uiState.value
        val bitmap = state.personalColorPhotoBitmap

        if (!state.isPersonalColorAnalyzing || bitmap == null) {
            uiState.value = state.copy(
                isPersonalColorAnalyzing = false,
                personalColorResult = null,
                personalColorAnalysisErrorMessage = "분석할 사진이 없어요. 다시 촬영해 주세요."
            )
            return false
        }

        if (!isRecognizablePhoto(bitmap)) {
            uiState.value = state.copy(
                isPersonalColorAnalyzing = false,
                personalColorResult = null,
                personalColorAnalysisErrorMessage = "얼굴을 인식할 수 없어요.\n얼굴이 정면으로 잘 보이도록 다시 촬영해 주세요."
            )
            return false
        }

        if (memberId == null) {
            loadMemberProfileFromServer(force = true)
        }

        state = uiState.value

        val safeMemberId = memberId
        if (safeMemberId == null) {
            uiState.value = state.copy(
                isPersonalColorAnalyzing = false,
                personalColorResult = null,
                personalColorAnalysisErrorMessage = "회원 정보를 불러오지 못했어요. 로그아웃 후 다시 로그인한 뒤 진행해 주세요."
            )
            return false
        }

        val repository = diagnosisRepository
        if (repository == null) {
            uiState.value = state.copy(
                isPersonalColorAnalyzing = false,
                personalColorResult = null,
                personalColorAnalysisErrorMessage = "진단 API 연결 정보가 없습니다."
            )
            return false
        }

        Log.d(
            TAG,
            "퍼스널 컬러 분석 요청 시작: memberId=$safeMemberId"
        )

        val apiResult = runCatching {
            repository.analyzePersonalColor(
                memberId = safeMemberId,
                bitmap = bitmap,
                userDisplayName = state.userDisplayName
            )
        }.getOrElse { error ->
            Log.e(
                TAG,
                "퍼스널 컬러 분석 요청 실패: ${error.toDiagnosisLogMessage()}",
                error
            )

            uiState.value = state.copy(
                isPersonalColorAnalyzing = false,
                personalColorResult = null,
                personalColorAnalysisErrorMessage = error.toAnalysisErrorMessage(
                    defaultMessage = "퍼스널 컬러 분석 서버 연결에 실패했어요. 잠시 후 다시 시도해 주세요."
                )
            )
            return false
        }

        uiState.value = state.copy(
            isPersonalColorAnalyzing = false,
            personalColorResult = apiResult,
            personalColorAnalysisErrorMessage = null
        )

        Log.d(TAG, "퍼스널 컬러 분석 요청 성공")

        return true
    }

    fun resetPersonalColorPhoto() {
        uiState.value = uiState.value.copy(
            personalColorPhotoBitmap = null,
            isPersonalColorAnalyzing = false,
            personalColorResult = null,
            personalColorAnalysisErrorMessage = null
        )
    }

    private fun isRecognizablePhoto(bitmap: Bitmap): Boolean {
        if (bitmap.width < 80 || bitmap.height < 80) {
            return false
        }

        val stepX = (bitmap.width / 20).coerceAtLeast(1)
        val stepY = (bitmap.height / 20).coerceAtLeast(1)

        var sampleCount = 0
        var brightnessSum = 0.0
        var brightnessSquareSum = 0.0

        var y = 0
        while (y < bitmap.height) {
            var x = 0
            while (x < bitmap.width) {
                val pixel = bitmap.getPixel(x, y)

                val red = (pixel shr 16) and 0xFF
                val green = (pixel shr 8) and 0xFF
                val blue = pixel and 0xFF

                val brightness = (red + green + blue) / 3.0

                brightnessSum += brightness
                brightnessSquareSum += brightness * brightness
                sampleCount++

                x += stepX
            }

            y += stepY
        }

        if (sampleCount == 0) {
            return false
        }

        val averageBrightness = brightnessSum / sampleCount
        val variance =
            (brightnessSquareSum / sampleCount) - (averageBrightness * averageBrightness)

        return averageBrightness in 35.0..225.0 && variance >= 40.0
    }

    private fun Throwable.toDiagnosisLogMessage(): String {
        return when (this) {
            is HttpException -> {
                val errorBody = response()
                    ?.errorBody()
                    ?.string()

                "HTTP ${code()}, body=$errorBody"
            }

            is IOException -> {
                "네트워크 오류: ${message}"
            }

            else -> {
                message ?: "알 수 없는 오류"
            }
        }
    }

    private fun Throwable.toAnalysisErrorMessage(
        defaultMessage: String
    ): String {
        return when (this) {
            is HttpException -> {
                when (code()) {
                    400 -> "진단 요청값이 서버 형식과 맞지 않아요. 이미지 필드명 또는 입력값을 확인해 주세요."
                    401 -> "로그인 정보가 만료되었어요. 다시 로그인한 뒤 진행해 주세요."
                    403 -> "진단 요청 권한이 없어요. 로그인 상태를 확인해 주세요."
                    404 -> "진단 API 경로를 찾지 못했어요. 백엔드 API 주소를 확인해 주세요."
                    in 500..599 -> "진단 서버 오류가 발생했어요. 잠시 후 다시 시도해 주세요."
                    else -> defaultMessage
                }
            }

            is IOException -> {
                "서버에 연결하지 못했어요. 인터넷 연결 상태를 확인해 주세요."
            }

            else -> {
                defaultMessage
            }
        }
    }

    companion object {
        private const val TAG = "DiagnosisViewModel"
    }
}