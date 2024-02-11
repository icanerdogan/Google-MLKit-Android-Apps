package com.ibrahimcanerdogan.textrecognition

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ibrahimcanerdogan.textrecognition.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val (bitmap, uri) = getImageFromIntent()
        setImage(uri, bitmap)

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setImage(uri: String?, bitmap: Bitmap?) {
        binding.apply {
            imageView.setImageDrawable(null)
            if (uri != null) {
                imageView.setImageURI(Uri.parse(uri))
            } else {
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun getImageFromIntent(): Pair<Bitmap?, String?> {
        val intent = getIntent()
        val bitmap = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getParcelableExtra("bitmapimage", Bitmap::class.java)
         else intent.getParcelableExtra("bitmapimage")

        val uri = intent.getStringExtra("uriimage")
        return Pair(bitmap, uri)
    }
}