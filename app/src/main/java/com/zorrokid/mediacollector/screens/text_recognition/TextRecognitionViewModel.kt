package com.zorrokid.mediacollector.screens.text_recognition

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.text.Text
import com.zorrokid.mediacollector.common.text_recognition.TextRecognitionAnalyzer
import com.zorrokid.mediacollector.model.service.LogService
import com.zorrokid.mediacollector.screens.MediaCollectorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextRecognitionViewModel @Inject constructor(
    logService: LogService
) : MediaCollectorViewModel(logService) {

    var uiState = mutableStateOf(TextRecognitionUiState())
        private set

    private val recognizedText
        get() = uiState.value.recognizedText

    private val pointerOffset
        get() = uiState.value.pointerOffset

    fun onPointerOffsetChanged(newOffset: Offset) {
        uiState.value = uiState.value.copy(pointerOffset = newOffset)
    }

    fun onDetectedTextUpdated(text: Text, imageWidth: Int, imageHeight: Int) {
        uiState.value = uiState.value.copy(
            recognizedText = text,
            imageHeight = imageHeight,
            imageWidth = imageWidth
        )
    }
    fun startTextRecognition(
        context: Context,
        cameraController: LifecycleCameraController,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
    ) {
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            TextRecognitionAnalyzer(
                onDetectedTextUpdated = ::onDetectedTextUpdated,
            )
        )
        cameraController.bindToLifecycle(lifecycleOwner)
        previewView.controller = cameraController
    }
}