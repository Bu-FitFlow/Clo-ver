package com.fitflow.clover.presentation.diagnosis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.fitflow.clover.core.network.NetworkModule
import com.fitflow.clover.data.repository.DiagnosisRepositoryImpl

@Composable
fun rememberDiagnosisViewModel(
    memberId: Long?,
    displayName: String?
): DiagnosisViewModel {
    val context = LocalContext.current

    val viewModel = remember(context) {
        val networkModule = NetworkModule(context)

        DiagnosisViewModel(
            diagnosisRepository = DiagnosisRepositoryImpl(
                diagnosisApi = networkModule.diagnosisApi
            )
        )
    }

    LaunchedEffect(memberId, displayName) {
        viewModel.setMemberProfile(
            memberId = memberId,
            displayName = displayName
        )
    }

    return viewModel
}
