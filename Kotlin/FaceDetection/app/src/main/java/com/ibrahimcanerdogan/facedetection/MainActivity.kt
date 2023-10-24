package com.ibrahimcanerdogan.facedetection

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ibrahimcanerdogan.facedetection.camera.CameraManager
import com.ibrahimcanerdogan.facedetection.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        cameraManager = CameraManager(
            this,
            binding.viewCameraPreview,
            binding.viewGraphicOverlay,
            this
        )

        askCameraPermission()
        buttonClick()
    }

    private fun buttonClick() {
        binding.apply {
            buttonTurnCamera.setOnClickListener {
                cameraManager.changeCamera()
            }

            buttonStopCamera.setOnClickListener {
                cameraManager.cameraStop()
                buttonStopCamera.visibility = View.INVISIBLE
                buttonStartCamera.visibility = View.VISIBLE
            }

            buttonStartCamera.setOnClickListener {
                cameraManager.cameraStart()
                buttonStopCamera.visibility = View.VISIBLE
                buttonStartCamera.visibility = View.INVISIBLE
            }
        }
    }

    private fun askCameraPermission() {
       if (arrayOf(android.Manifest.permission.CAMERA).all {
           ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
       }) {
           cameraManager.cameraStart()
       } else {
           ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 0)
       }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraManager.cameraStart()
        } else {
            Toast.makeText(this, "Camera Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }
}