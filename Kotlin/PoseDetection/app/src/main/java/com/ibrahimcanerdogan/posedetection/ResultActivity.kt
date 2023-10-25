package com.ibrahimcanerdogan.posedetection

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ibrahimcanerdogan.posedetection.databinding.ActivityResultBinding
import com.ibrahimcanerdogan.posedetection.utils.AlertDialog
import com.ibrahimcanerdogan.posedetection.utils.AngleInstance
import com.ibrahimcanerdogan.posedetection.utils.BitmapInstance
import com.ibrahimcanerdogan.posedetection.utils.ImageUtils

class ResultActivity : AppCompatActivity() {

    private val binding by lazy { ActivityResultBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            imageViewPose.setImageResource(0)
            imageViewPose.setImageBitmap(BitmapInstance.getInstance()?.getBitmap())

            buttonBack.setOnClickListener {
                imageViewPose.setImageResource(0)
                val intent = Intent(this@ResultActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            buttonDownload.setOnClickListener {
                ImageUtils.saveImage(BitmapInstance.getInstance()?.getBitmap()!!, this@ResultActivity)
            }

            buttonInfo.setOnClickListener {
                AlertDialog.informationDialog(
                    context = this@ResultActivity,
                    title = "Angle Information",
                    message = AngleInstance.getInstance()?.getAngle()
                )
            }
        }
    }
}