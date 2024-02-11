package com.ibrahimcanerdogan.selfiesegmentation.fragment

import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.ibrahimcanerdogan.selfiesegmentation.R
import com.ibrahimcanerdogan.selfiesegmentation.databinding.FragmentStreamModeBinding
import com.ibrahimcanerdogan.selfiesegmentation.utils.DrawOverlay
import com.ibrahimcanerdogan.selfiesegmentation.utils.PermissionCheckerUtils
import java.util.concurrent.Executors

class StreamModeFragment : Fragment() {

    private var _binding : FragmentStreamModeBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraProvider : ListenableFuture<ProcessCameraProvider>
    private lateinit var drawOverlay: DrawOverlay
    private lateinit var streamAnalyzer: StreamAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionCheckerUtils.checkCameraPerm(requireContext(), requireActivity()) {
            setupCameraProvider()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamModeBinding.inflate(layoutInflater, container, false)
        drawOverlay = binding.drawOverlay
        drawOverlay.setWillNotDraw(false)
        drawOverlay.setZOrderOnTop(true)
        streamAnalyzer = StreamAnalyzer(drawOverlay)
        return binding.root
    }

    private fun setupCameraProvider() {
        cameraProvider = ProcessCameraProvider.getInstance(requireContext())
        cameraProvider.addListener({
            try {
                val cameraProvider : ProcessCameraProvider = cameraProvider.get()
                bindPreview(cameraProvider)
            } catch (e: Exception) {
                Log.e(TAG, e.printStackTrace().toString())
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val previewView = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        previewView.setSurfaceProvider(binding.previewCamera.surfaceProvider)
        val displayMetrics = resources.displayMetrics
        val screenSize = Size(displayMetrics.widthPixels, displayMetrics.heightPixels)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(screenSize)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(Executors.newSingleThreadScheduledExecutor(), streamAnalyzer)
        cameraProvider.bindToLifecycle((this as LifecycleOwner), cameraSelector, imageAnalysis, previewView)
    }

    override fun onStart() {
        super.onStart()
        PermissionCheckerUtils.checkCameraPerm(requireContext(), requireActivity()) {
            setupCameraProvider()
        }
    }

    override fun onResume() {
        super.onResume()
        PermissionCheckerUtils.checkCameraPerm(requireContext(), requireActivity()) {
            setupCameraProvider()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val TAG = "StreamModeFragment"
    }
}