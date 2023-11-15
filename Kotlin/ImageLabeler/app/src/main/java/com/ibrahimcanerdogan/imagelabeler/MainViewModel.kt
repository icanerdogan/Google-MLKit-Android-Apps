package com.ibrahimcanerdogan.imagelabeler

import android.content.Context
import android.media.Image
import android.media.ImageReader
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.ibrahimcanerdogan.imagelabeler.utils.AlertCreator

class MainViewModel(mContext: Context) : ViewModel() {

    private var alertCreator : AlertCreator

    private var image: Image? = null
    private var detectedText : String = ""

    init {
        alertCreator = AlertCreator(mContext)
    }

    val onImageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        image = reader.acquireLatestImage()
        detectLabel(image!!)
        image?.close()
    }

    private fun detectLabel(image: Image) {
        val inputImage : InputImage = InputImage.fromMediaImage(image, 0)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(inputImage)
            .addOnSuccessListener { labels ->
                for(label in labels) {
                    val text = label.text
                    val confidence = label.confidence
                    val newConfidence = confidence * 100

                    val strConfidence = String.format("%.2f", newConfidence)

                    detectedText += "$text - %$strConfidence \n"
                }
                alertCreator.create("Label Info", detectedText)
                detectedText = ""
            }
            .addOnFailureListener {
                Log.e(TAG, "Object Not Detected!")
            }
    }

    var orientations : SparseIntArray = SparseIntArray(4).apply {
        append(Surface.ROTATION_0, 0)
        append(Surface.ROTATION_90, 90)
        append(Surface.ROTATION_180, 180)
        append(Surface.ROTATION_270, 270)
    }

    companion object {
        private val TAG : String = MainViewModel::class.java.simpleName
    }
}