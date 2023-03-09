package com.ibrahimcanerdogan.textrecognitionapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ibrahimcanerdogan.textrecognitionapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private var viewModel : MainViewModel = MainViewModel()

    private var detectImage : Bitmap? = null
    private var detectImageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.movementMethod = ScrollingMovementMethod()
        binding.buttonCopy.bringToFront()

        copy()
        buttonClick()
    }

    private fun copy() {
        if (!binding.textView.text.isNullOrEmpty()) {
            binding.buttonCopy.setOnClickListener {
                val clipboard : ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("copied", binding.textView.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "Text Copied!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun buttonClick() {
        binding.buttonCamera.setOnClickListener {
            controlCameraPermission()
        }
        binding.buttonGallery.setOnClickListener {
            controlGalleryPermission()
        }
        binding.buttonSearch.setOnClickListener {
            detectImage?.let {
                Toast.makeText(this, "Re Recognition Image!", Toast.LENGTH_SHORT).show()
                setRecognitionTextFromImageView(it)
            }
        }
        binding.buttonShowImage.setOnClickListener {
            if (detectImage != null && detectImageUri != null) {
                val intent = Intent(this, MainActivity2::class.java)
                intent.putExtra("uriimage", detectImageUri.toString())
                startActivity(intent)
            } else if(detectImage != null) {
                val intent =Intent(this, MainActivity2::class.java)
                intent.putExtra("bitmapimage", detectImage)
            } else {
                Toast.makeText(this, "No images have been selected!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            detectImage = result.data?.extras?.get("data") as Bitmap
            setRecognitionTextFromImageView(detectImage!!)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val uri : Uri? = result.data?.data

        if (result.resultCode == RESULT_OK && uri != null) {
            detectImageUri = uri
            val inputStream = contentResolver.openInputStream(detectImageUri!!)
            detectImage = BitmapFactory.decodeStream(inputStream)
            setRecognitionTextFromImageView(detectImage!!)
        }
    }

    // Text Recognition

    private fun setRecognitionTextFromImageView(bitmap: Bitmap) {
        runBlocking {
            withContext(Dispatchers.Default) {
                viewModel.textRecognizer(this@MainActivity, binding.textView, bitmap)
            }
        }
    }
    // Permissions

    private fun controlCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            openCamera()
        }
    }

    private fun controlGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                GALLERY_PERMISSION_CODE
            )
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera Permission Denied!", Toast.LENGTH_SHORT).show()
                }
            }
            GALLERY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Gallery Permission Denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE : Int = 0
        private const val GALLERY_PERMISSION_CODE : Int = 1

    }
}