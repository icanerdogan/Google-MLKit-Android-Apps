package com.ibrahimcanerdogan.posedetection

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.ibrahimcanerdogan.posedetection.utils.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.ibrahimcanerdogan.posedetection.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel : PoseViewModel by viewModels()
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            // Image from gallery.
            buttonGallery.setOnClickListener {
                if (allPermissionGranted()) {
                    selectImageFromGallery()
                } else {
                    ActivityCompat.requestPermissions(this@MainActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
                }
            }

            // Clear image & text.
            buttonClear.setOnClickListener {
                if (imageViewPreviewPhoto.drawable != null) {
                    AlertDialog.make(
                        context = this@MainActivity,
                        title = "Make a Choice!",
                        message = "Do you confirm?",
                        positive = {
                            imageViewPreviewPhoto.setImageResource(0)
                            textViewChooseGallery.text = resources.getString(R.string.choose_image_from_gallery)
                        },
                        negative = {
                            Toast.makeText(this@MainActivity, "Clear process canceled!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // Analyse image.
            buttonSend.setOnClickListener {
                if (imageViewPreviewPhoto.drawable == null) {
                    Toast.makeText(this@MainActivity, "Preview image is empty!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Pose is being processed...", Toast.LENGTH_SHORT).show()
                    viewModel.runPose(this@MainActivity, imageViewPreviewPhoto.drawable.toBitmap())
                    viewModel.calculatePose(imageViewPreviewPhoto.drawable.toBitmap())
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@MainActivity, ResultActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000)
                }
            }
        }
    }

    // Gallery
    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
        uri?.let {
            binding.imageViewPreviewPhoto.setImageURI(it)
            binding.textViewChooseGallery.text = ""
        }
    }

    // Permission

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION) {
            selectImageFromGallery()
        } else {
            Toast.makeText(this, "Permissions Not Granted!", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val REQUEST_CODE_PERMISSION = 0
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}