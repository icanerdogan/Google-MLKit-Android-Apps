package com.ibrahimcanerdogan.imagelabeler

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ibrahimcanerdogan.imagelabeler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(this)
    }
    // Camera
    private lateinit var previewSize : Size
    private lateinit var backgroundHandler : Handler
    private lateinit var backgroundHandlerThread : HandlerThread
    private lateinit var imageReader: ImageReader
    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraManager: CameraManager
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var cameraCaptureSession: CameraCaptureSession

    private lateinit var cameraId: String
    private var shouldProceedWithOnResume : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonCamera.setOnClickListener { takePhoto() }
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        if (!isCameraPermissionGiven()) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_RESULT)
        }
        startBackgroundThread()
    }

    @SuppressLint("MissingPermission")
    private fun connectCamera() {
        cameraManager.openCamera(cameraId, cameraStateCallback, backgroundHandler)
    }

    private fun setupCamera() {
        val cameraIds : Array<String> = cameraManager.cameraIdList

        for (id in cameraIds) {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(id)

            if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) continue

            val streamConfigurationMap : StreamConfigurationMap? = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            if (streamConfigurationMap != null) {
                previewSize = cameraCharacteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                    .getOutputSizes(ImageFormat.YUV_420_888)
                    .maxByOrNull {it.height * it.width }!!

                imageReader = ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.JPEG, 1)
                imageReader.setOnImageAvailableListener(viewModel.onImageAvailableListener, backgroundHandler)
            }
            cameraId = id
        }
    }

    // Surface Texture Listener
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
            if (isCameraPermissionGiven()) {
                setupCamera()
                connectCamera()
            }
        }

        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}
        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean { return true }
        override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}
    }

    private fun startBackgroundThread() {
        backgroundHandlerThread = HandlerThread("CameraVideoThread")
        backgroundHandlerThread.start()
        backgroundHandler = Handler(backgroundHandlerThread.looper)
    }

    private fun takePhoto() {
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequestBuilder.addTarget(imageReader.surface)
        val rotation = windowManager.defaultDisplay.rotation

        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, viewModel.orientations.get(rotation))
        cameraCaptureSession.capture(captureRequestBuilder.build(), null, null)
    }

    // Camera State Callback
    private val cameraStateCallback = object  : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            val surfaceTexture : SurfaceTexture? = binding.textureView.surfaceTexture
            surfaceTexture?.setDefaultBufferSize(previewSize.width, previewSize.height)
            val previewSurface = Surface(surfaceTexture)

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder.addTarget(previewSurface)

            cameraDevice.createCaptureSession(listOf(previewSurface, imageReader.surface), captureStateCallback, null)
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraDevice.close()
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            val errorMessage = when(error) {
                ERROR_CAMERA_DEVICE -> "Fatal (Device)"
                ERROR_CAMERA_DISABLED -> "Device Policy"
                ERROR_CAMERA_IN_USE -> "Camera In Use"
                ERROR_CAMERA_SERVICE -> "Fatal (Service)"
                ERROR_MAX_CAMERAS_IN_USE -> "Maximum Cameras In Use"
                else -> "UNKNOWN"
            }
            Log.e(TAG, "Error when trying to connect camera $errorMessage")
        }

    }

    private val captureStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            cameraCaptureSession = session
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {}
    }

    // Permission
    private fun isCameraPermissionGiven(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isCameraPermissionGiven()) {
            surfaceTextureListener.onSurfaceTextureAvailable(binding.textureView.surfaceTexture!!, binding.textureView.width, binding.textureView.height)
        } else {
            Toast.makeText(this, "Camera permission is needed to run this application!", Toast.LENGTH_SHORT).show()
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", this.packageName, null)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if (binding.textureView.isAvailable && shouldProceedWithOnResume) {
            setupCamera()
        } else if (!binding.textureView.isAvailable) {
            binding.textureView.surfaceTextureListener = surfaceTextureListener
        }
        shouldProceedWithOnResume = !shouldProceedWithOnResume
    }

    companion object {
        const val CAMERA_REQUEST_RESULT = 1
        val TAG = MainActivity::class.java.simpleName
    }
}