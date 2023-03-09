package com.ibrahimcanerdogan.facedetectionapp.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.ibrahimcanerdogan.facedetectionapp.graphic.GraphicOverlay
import com.ibrahimcanerdogan.facedetectionapp.utils.CameraUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val context : Context,
    private val previewView : PreviewView,
    private val graphicOverlay: GraphicOverlay<*>,
    private val lifecycleOwner : LifecycleOwner
) {

   private lateinit var cameraProvider: ProcessCameraProvider
   private lateinit var preview : Preview
   private lateinit var imageAnalysis: ImageAnalysis
   private lateinit var camera : Camera
   private var cameraExecutor : ExecutorService = Executors.newSingleThreadExecutor()

    fun cameraStart() {
        val cameraProcessProvider = ProcessCameraProvider.getInstance(context)

        cameraProcessProvider.addListener(
            {
                cameraProvider = cameraProcessProvider.get()
                preview = Preview.Builder().build()

                imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, CameraAnalyzer(graphicOverlay))
                    }
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraOption)
                    .build()

                setCameraConfig(cameraProvider, cameraSelector)
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    private fun setCameraConfig(cameraProvider: ProcessCameraProvider, cameraSelector: CameraSelector) {
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
        } catch (e : Exception) {
            Log.e(TAG, "setCameraConfig : $e")
        }
    }

    fun changeCamera() {
        cameraStop()
        cameraOption = if (cameraOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
        CameraUtils.toggleSelector()
        cameraStart()
    }

    fun cameraStop () {
        cameraProvider.unbindAll()
    }
    
    companion object {
        private const val TAG : String = "CameraManager"
        var cameraOption : Int = CameraSelector.LENS_FACING_FRONT
    }
}