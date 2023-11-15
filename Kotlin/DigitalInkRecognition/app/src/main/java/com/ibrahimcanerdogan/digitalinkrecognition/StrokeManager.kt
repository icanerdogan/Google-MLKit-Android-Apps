package com.ibrahimcanerdogan.digitalinkrecognition

import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink

object StrokeManager {

    private val TAG = StrokeManager.javaClass.simpleName

    private var inkBuilder : Ink.Builder = Ink.builder()
    private lateinit var inkStrokeBuilder : Ink.Stroke.Builder
    private lateinit var digitalInkRecognitionModel : DigitalInkRecognitionModel

    fun addNewTouchEvent(event : MotionEvent) {
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()

        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                inkStrokeBuilder = Ink.Stroke.builder()
                inkStrokeBuilder.addPoint(Ink.Point.create(x, y, t))
            }
            MotionEvent.ACTION_MOVE -> inkStrokeBuilder.addPoint(Ink.Point.create(x, y, t))
            MotionEvent.ACTION_UP -> {
                inkStrokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(inkStrokeBuilder.build())
            }
        }
    }

    fun downloadInkRecognition() {
        var modelIdentifier : DigitalInkRecognitionModelIdentifier? = null
        try {
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        } catch (e: Exception) {
            Log.e(TAG, "Exception:$e")
        }

        modelIdentifier?.let {
            digitalInkRecognitionModel = DigitalInkRecognitionModel.builder(it).build()
        }

        val remoteModelManager : RemoteModelManager = RemoteModelManager.getInstance()
        remoteModelManager
            .download(digitalInkRecognitionModel, DownloadConditions.Builder().build())
            .addOnSuccessListener { Log.i(TAG, "Model Downloaded!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error while downloading a model : $e") }
    }

    fun drawRecognizer(textView: AppCompatTextView) {
        val recognizer : DigitalInkRecognizer = DigitalInkRecognition.getClient(
            DigitalInkRecognizerOptions.builder(digitalInkRecognitionModel).build()
        )

        val ink : Ink = inkBuilder.build()
        recognizer.recognize(ink)
            .addOnSuccessListener { result ->
                textView.text = result.candidates[0].text
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Exception $e")
            }
    }

    fun clear() {
        inkBuilder = Ink.builder()
    }
}