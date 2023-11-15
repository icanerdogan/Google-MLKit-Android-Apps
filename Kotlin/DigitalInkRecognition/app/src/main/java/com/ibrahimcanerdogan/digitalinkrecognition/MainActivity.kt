package com.ibrahimcanerdogan.digitalinkrecognition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.ibrahimcanerdogan.digitalinkrecognition.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            textView.movementMethod = ScrollingMovementMethod()
            StrokeManager.downloadInkRecognition()

            buttonRecognize.setOnClickListener {
                textView.visibility = View.VISIBLE
                buttonRecognize.visibility = View.INVISIBLE
                StrokeManager.drawRecognizer(textView)
            }

            buttonClear.setOnClickListener {
                drawView.clear()
                StrokeManager.clear()
                textView.visibility = View.INVISIBLE
                buttonRecognize.visibility = View.VISIBLE
            }
        }
    }
}