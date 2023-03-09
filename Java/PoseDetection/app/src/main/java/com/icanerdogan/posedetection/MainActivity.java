package com.icanerdogan.posedetection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.InputStream;

import static java.lang.Math.atan2;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout cameraButton, galleryButton;
    CameraView cameraViewPose;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraButton = findViewById(R.id.cameraBtn);
        galleryButton = findViewById(R.id.galleryBtn);
        cameraViewPose = findViewById(R.id.poseCamera);

        // Progress Bar
        progressBar = findViewById(R.id.spin_kit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        progressBar.setVisibility(View.INVISIBLE);

        // OnClick
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);


        cameraViewPose.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                progressBar.setVisibility(View.VISIBLE);
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraViewPose.getWidth(), cameraViewPose.getHeight(), false);
                cameraViewPose.stop();

                runPose(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    // OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cameraBtn:
                poseDetect();
                break;
            case R.id.galleryBtn:
                galleryAdd();
                break;
        }
    }

    // Galeri Fonksiyonları
    final int REQUEST_GALLERY = 1;
    private void galleryAdd() {
        Intent mediaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mediaIntent, REQUEST_GALLERY);
    }

    Bitmap galleryImage;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_GALLERY:
                try {
                    Uri imageUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    galleryImage = BitmapFactory.decodeStream(inputStream);
                    runPose(galleryImage);
                    progressBar.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Görsel Galeriden Alınamadı",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    // Kamera Fonksiyonları
    private void poseDetect() {
        cameraViewPose.start();
        cameraViewPose.captureImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraViewPose.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraViewPose.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraViewPose.stop();
    }

    // Pose Detect
    AccuratePoseDetectorOptions options =
            new AccuratePoseDetectorOptions.Builder()
                    .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                    .build();

    PoseDetector poseDetector = PoseDetection.getClient(options);

    Bitmap resizedBitmap;

    private void runPose(Bitmap bitmap) {

        int rotationDegree = 0;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

        InputImage image = InputImage.fromBitmap(resizedBitmap, rotationDegree);

        poseDetector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<Pose>() {
                            @Override
                            public void onSuccess(Pose pose) {
                                processPose(pose);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Pose Tespit Edilemedi",Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    // Pose Detect
    String angleText;

    private void processPose(Pose pose) {
        try {

            // Omuz
            PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
            PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);

            // Kol Dirsekleri
            PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
            PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);

            // El Bileklerimiz
            PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
            PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);

            // Kalça
            PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
            PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);

            // Ayak Dizleri
            PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
            PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);

            // Ayak Bilekleri
            PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
            PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);


            //Omuz
            PointF leftShoulderP = leftShoulder.getPosition();
            float lShoulderX = leftShoulderP.x;
            float lShoulderY = leftShoulderP.y;
            PointF rightSoulderP = rightShoulder.getPosition();
            float rShoulderX = rightSoulderP.x;
            float rShoulderY = rightSoulderP.y;

            //Dirsek
            PointF leftElbowP = leftElbow.getPosition();
            float lElbowX = leftElbowP.x;
            float lElbowY = leftElbowP.y;
            PointF rightElbowP = rightElbow.getPosition();
            float rElbowX = rightElbowP.x;
            float rElbowY = rightElbowP.y;

            // El Bilek
            PointF leftWristP = leftWrist.getPosition();
            float lWristX = leftWristP.x;
            float lWristY = leftWristP.y;
            PointF rightWristP = rightWrist.getPosition();
            float rWristX = rightWristP.x;
            float rWristY = rightWristP.y;

            // Kalça
            PointF leftHipP = leftHip.getPosition();
            float lHipX = leftHipP.x;
            float lHipY = leftHipP.y;
            PointF rightHipP = rightHip.getPosition();
            float rHipX = rightHipP.x;
            float rHipY = rightHipP.y;

            //Diz
            PointF leftKneeP = leftKnee.getPosition();
            float lKneeX = leftKneeP.x;
            float lKneeY = leftKneeP.y;
            PointF rightKneeP = rightKnee.getPosition();
            float rKneeX = rightKneeP.x;
            float rKneeY = rightKneeP.y;

            // Ayak Bileği
            PointF leftAnkleP = leftAnkle.getPosition();
            float lAnkleX = leftAnkleP.x;
            float lAnkleY = leftAnkleP.y;
            PointF rightAnkleP = rightAnkle.getPosition();
            float rAnkleX = rightAnkleP.x;
            float rAnkleY = rightAnkleP.y;

            // Angle Text
            double leftArmAngle = getAngle(leftShoulder, leftElbow,leftWrist);
            String leftArmAngleText = String.format("%.2f", leftArmAngle);

            double rightArmAngle = getAngle(rightShoulder, rightElbow,rightWrist);
            String rightArmAngleText = String.format("%.2f", rightArmAngle);

            double leftLegAngle = getAngle(leftHip, leftKnee, leftAnkle);
            String leftLegAngleText = String.format("%.2f", leftLegAngle);

            double rightLegAngle = getAngle(rightHip, rightKnee, rightAnkle);
            String rightLegAngleText = String.format("%.2f", rightLegAngle);

            angleText = "Sol Kol Açısı: "+leftArmAngleText+"\n" +
                    "Sağ Kol Açısı: "+rightArmAngleText + "\n" +
                    "Sol Ayak Açısı: "+leftLegAngleText + "\n" +
                    "Sağ Ayak Açısı: "+rightLegAngleText;


            DisplayAll(lShoulderX, lShoulderY, rShoulderX, rShoulderY,
                    lElbowX, lElbowY, rElbowX, rElbowY,
                    lWristX, lWristY, rWristX, rWristY,
                    lHipX, lHipY, rHipX, rHipY,
                    lKneeX, lKneeY, rKneeX, rKneeY,
                    lAnkleX, lAnkleY, rAnkleX,rAnkleY);



        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Pose Tespit Edilemedi",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    // Pose Draw
    private void DisplayAll(float lShoulderX, float lShoulderY, float rShoulderX, float rShoulderY,
                            float lElbowX, float lElbowY, float rElbowX, float rElbowY,
                            float lWristX, float lWristY, float rWristX, float rWristY,
                            float lHipX, float lHipY, float rHipX, float rHipY,
                            float lKneeX, float lKneeY, float rKneeX, float rKneeY,
                            float lAnkleX, float lAnkleY, float rAnkleX, float rAnkleY) {

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        float strokeWidth = 4.0f;
        paint.setStrokeWidth(strokeWidth);

        Bitmap drawBitmap = Bitmap.createBitmap(resizedBitmap.getWidth(), resizedBitmap.getHeight(), resizedBitmap.getConfig());

        Canvas canvas =new Canvas(drawBitmap);

        canvas.drawBitmap(resizedBitmap, 0f, 0f, null);

        // Sol Omuzdan Sağ Omuza
        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint);

        // Sağ Omuzdan Sağ Dirseğe
        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint);

        //Sağ Dirsekten Sağ El Bileğine
        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, paint);

        // Sol Omuzdan Sol Dirseğe
        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint);

        // Sol Dirsekten Sol El Bileğine
        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, paint);

        //Sağ Omuzdan Sağ Kalçaya
        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, paint);

        // Sol Omuzdan Sol Kalçaya
        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, paint);

        // Kalça (Bel)
        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, paint);

        // Sağ Kalçadan Sağ Ayak Dizine
        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, paint);

        // Sol Kalçadan Sol Ayak Dizine
        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, paint);

        // Sağ Ayak Dizinden Sağ Ayak Bileğine
        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, paint);

        // Sol Ayak Dizinden Sol Ayak Bileğine
        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, paint);

        // MainActivity to MainActivity2
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("Text", angleText);

        Singleton singleton = Singleton.getInstance();
        singleton.setMyImage(drawBitmap);

        startActivity(intent);

    }

    // Angle Detect
    static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result =
                Math.toDegrees(
                        atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }
}