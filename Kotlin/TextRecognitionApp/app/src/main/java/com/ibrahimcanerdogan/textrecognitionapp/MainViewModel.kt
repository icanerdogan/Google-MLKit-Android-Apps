package com.ibrahimcanerdogan.textrecognitionapp

import android.content.Context
import android.graphics.Bitmap
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainViewModel : ViewModel() {

    fun textRecognizer(context : Context, textView: TextView, bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                processText(context, textView, visionText)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Text could not be read!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun processText(context: Context, textView: TextView, visionText: Text) {
        val blocks = visionText.textBlocks
        if (blocks.size == 0) {
            Toast.makeText(context, "No Text Detected In Image!", Toast.LENGTH_SHORT).show()
        }

        val text = StringBuilder()
        for (block in blocks) {
            for (line in block.lines) {
                for (element in line.elements) {
                    text.append(element.text + " ")
                }
            }
        }

        textView.text = text.toString()
    }
}