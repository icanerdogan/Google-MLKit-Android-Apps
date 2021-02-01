package com.icanerdogan.imagelabeler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    JCameraView cameraView;

    private boolean granted = false;
    private final int GET_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PROGRESS BAR
        progressBar = findViewById(R.id.spin_kit);
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setVisibility(View.INVISIBLE);

        // KAMERA
        cameraView = findViewById(R.id.jCameraView);

        cameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        cameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);

        cameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);

        cameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "Kamera Açılmadı",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(MainActivity.this, "Ses İzin Hatası", Toast.LENGTH_SHORT).show();
            }
        });

        cameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                progressBar.setVisibility(View.VISIBLE);
                runLabeling(bitmap);
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {

            }
        });

        getPermissions();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
    }

    // KAMERA İZİNLERİ

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                granted = true;
            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
                granted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;
                if (!writeGranted) {
                    size++;
                }
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    granted = true;
                    cameraView.onResume();
                }else{
                    Toast.makeText(this, "İzinlerinizi Kontrol Ediniz", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    // GÖRSEL TESPİTİ

    String detectedText = "";
    private void runLabeling(Bitmap bitmap) {
        int rotationDegree = 0;

        InputImage image = InputImage.fromBitmap(bitmap, rotationDegree);

        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        for (ImageLabel label : labels) {

                            String text = label.getText();
                            float confidence = label.getConfidence();

                            float confidence2 = confidence * 100;
                            String strConfidence = String.format("%.2f", confidence2);

                            detectedText = text + " - %"+strConfidence +"\n" + detectedText;

                        }

                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                        intent.putExtra("detectText",detectedText);

                        Singleton singleton = Singleton.getInstance();
                        singleton.setMyImage(bitmap);

                        startActivity(intent);

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Görselde Cisim Tespit Edilemedi",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}