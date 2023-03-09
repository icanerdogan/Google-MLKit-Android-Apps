package com.ibrahimcanerdogan.textrecognitionapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ibrahimcanerdogan.textrecognitionapp.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val (bitmap, uri) = getImageFormatFromIntent()
        setImage(uri, bitmap)

        binding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setImage(uri: String?, bitmap: Bitmap?) {
        binding.imageView.setImageDrawable(null)
        if (uri != null) {
            binding.imageView.setImageURI(Uri.parse(uri))
        } else {
            binding.imageView.setImageBitmap(bitmap)
        }
    }

    private fun getImageFormatFromIntent() : Pair<Bitmap?, String?> {
        val intent : Intent = getIntent()

        val bitmap = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) intent.getParcelableExtra("bitmapimage", Bitmap::class.java)
            else intent.getParcelableExtra("bitmapimage")

        val uri = intent.getStringExtra("uriimage")

        return Pair(bitmap, uri)
    }
}