package com.ibrahimcanerdogan.posedetection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import com.ibrahimcanerdogan.posedetection.utils.AngleInstance
import com.ibrahimcanerdogan.posedetection.utils.BitmapInstance
import kotlin.math.abs
import kotlin.math.atan2

class PoseViewModel : ViewModel() {

    // Pose Options
    private var options : AccuratePoseDetectorOptions = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
        .build()

    private val poseDetector = PoseDetection.getClient(options)

    // Pose Detect

    fun runPose(context: Context, bitmap: Bitmap) {
        val rotationDegree = 0
        val image = InputImage.fromBitmap(bitmap, rotationDegree)

        poseDetector.process(image)
            .addOnSuccessListener { pose ->
                processPose(context, bitmap, pose)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Pose Not Detected!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun processPose(context: Context, bitmap: Bitmap, pose: Pose) {
        try {
            // Shoulder
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

            val leftShoulderP = leftShoulder!!.position
            val lShoulderX = leftShoulderP.x
            val lShoulderY = leftShoulderP.y
            val rightShoulderP = rightShoulder!!.position
            val rShoulderX = rightShoulderP.x
            val rShoulderY = rightShoulderP.y

            // Elbow
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)

            val leftElbowP = leftElbow!!.position
            val lElbowX = leftElbowP.x
            val lElbowY = leftElbowP.y
            val rightElbowP = rightElbow!!.position
            val rElbowX = rightElbowP.x
            val rElbowY = rightElbowP.y

            // Wrist
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

            val leftWristP = leftWrist!!.position
            val lWristX = leftWristP.x
            val lWristY = leftWristP.y
            val rightWristP = rightWrist!!.position
            val rWristX = rightWristP.x
            val rWristY = rightWristP.y

            // Hip
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)

            val leftHipP = leftHip!!.position
            val lHipX = leftHipP.x
            val lHipY = leftHipP.y
            val rightHipP = rightHip!!.position
            val rHipX = rightHipP.x
            val rHipY = rightHipP.y

            // Knee
            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)

            val leftKneeP = leftKnee!!.position
            val lKneeX = leftKneeP.x
            val lKneeY = leftKneeP.y
            val rightKneeP = rightKnee!!.position
            val rKneeX = rightKneeP.x
            val rKneeY = rightKneeP.y

            // Ankle
            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            val leftAnkleP = leftAnkle!!.position
            val lAnkleX = leftAnkleP.x
            val lAnkleY = leftAnkleP.y
            val rightAnkleP = rightAnkle!!.position
            val rAnkleX = rightAnkleP.x
            val rAnkleY = rightAnkleP.y

            drawAllPose(bitmap, lShoulderX, lShoulderY, rShoulderX, rShoulderY,
                lElbowX, lElbowY, rElbowX, rElbowY,
                lWristX, lWristY, rWristX, rWristY,
                lHipX, lHipY, rHipX, rHipY,
                lKneeX, lKneeY, rKneeX, rKneeY,
                lAnkleX, lAnkleY, rAnkleX, rAnkleY
            )
        } catch (e : Exception) {
            Toast.makeText(context, "Pose Not Detected!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawAllPose(bitmap: Bitmap,
        lShoulderX: Float, lShoulderY: Float, rShoulderX: Float, rShoulderY: Float,
        lElbowX: Float, lElbowY: Float, rElbowX: Float, rElbowY: Float,
        lWristX: Float, lWristY: Float, rWristX: Float, rWristY: Float,
        lHipX: Float, lHipY: Float, rHipX: Float, rHipY: Float,
        lKneeX: Float, lKneeY: Float, rKneeX: Float, rKneeY: Float,
        lAnkleX: Float, lAnkleY: Float, rAnkleX: Float, rAnkleY: Float
    ) {
        val paint = Paint()
        paint.color = Color.GREEN
        val strokeWidth = 4.0f
        paint.strokeWidth = strokeWidth

        val drawBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)

        val canvas = Canvas(drawBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint) // Left Shoulder to Right Shoulder
        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint) // Right Shoulder to Right Elbow
        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, paint) // Right Elbow to Right Wrist
        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint) // Left Shoulder to Left Elbow
        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, paint) // Left Elbow to Left Wrist
        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, paint) // Right Shoulder to Right Hip
        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, paint) // Left Shoulder to Left Hip
        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, paint) // Hip
        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, paint) // Right Hip To Right Foot Knee
        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, paint) // Left Hip to Left Foot Knee
        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, paint) // Right Foot Knee to Right Ankle
        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, paint) // Left Foot Knee to Left Ankle

        BitmapInstance.getInstance()?.setBitmap(drawBitmap)
    }

    // Angle Detect

    fun calculatePose(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        poseDetector.process(image)
            .addOnSuccessListener {pose ->
                getAnglesInfo(pose)
            }
    }

    private fun getAnglesInfo(pose: Pose) {
        try {
            // Shoulder
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

            // Elbow
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)

            // Wrist
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

            // Hip
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)

            // Knee
            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)

            // Ankle
            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            val leftArmAngle : String = String.format("%.2f", setAngleImage(leftShoulder!!, leftElbow!!, leftWrist!!))
            val rightArmAngle : String = String.format("%.2f", setAngleImage(rightShoulder!!, rightElbow!!, rightWrist!!))
            val leftLegAngle : String = String.format("%.2f", setAngleImage(leftHip!!, leftKnee!!, leftAnkle!!))
            val rightLegAngle : String = String.format("%.2f", setAngleImage(rightHip!!, rightKnee!!, rightAnkle!!))

            val angle = "Left Arm Angle : $leftArmAngle° \n" +
                    "Right Arm Angle: $rightArmAngle° \n" +
                    "Left Leg Angle: $leftLegAngle° \n" +
                    "Right Leg Angle: $rightLegAngle°"

            AngleInstance.getInstance()?.setAngle(angle)

        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setAngleImage(
        firstPoint: PoseLandmark,
        midPoint: PoseLandmark,
        lastPoint: PoseLandmark
    ) : Double {
        var result = Math.toDegrees(
            atan2(
                (lastPoint.position.y - midPoint.position.y).toDouble(),
                (lastPoint.position.x - midPoint.position.x).toDouble()
            ) -
            atan2(
                (firstPoint.position.y - midPoint.position.y).toDouble(),
                (firstPoint.position.x - midPoint.position.x).toDouble()
            )
        )

        result = abs(result)
        if (result > 180) result = (360.0 - result)
        return result
    }
}