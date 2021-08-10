package com.icanerdogan.objecttracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;
import com.icanerdogan.objecttracking.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    // Binding Nesnesi Oluşturma
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideTitleBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding Bağlama
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // BASİTÇE GEREKLİ İZİNLERİN ALINMASI
        Dexter.withContext(this)
                .withPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        trackingCamera();
                        Toasty.normal(MainActivity.this, "Hoşgeldiniz",Toasty.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        Toasty.warning(MainActivity.this, "Gerekli İzinler Verilmedi!", Toast.LENGTH_SHORT, true).show();
                    }
                }).check();

    }



    // TITLE BAR GİZLEME
    private void hideTitleBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        hideTitleBar();
    }


    // OBJE TAKİP ÖZELLİKLERİ
    ObjectDetectorOptions options =
            new ObjectDetectorOptions.Builder()
                    .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                    .enableClassification()  // Optional
                    .build();

    ObjectDetector objectDetector = ObjectDetection.getClient(options);

    // KAMERA ÇALIŞTIRMA FONKSİYONU
    private void trackingCamera() {
        binding.cameraView.setLifecycleOwner(this);
        binding.cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull @NotNull Frame frame) {
                processImage(getInputImageFromFrame(frame));
            }
        });
    }
    // VIDEODAN IMAGE ELDESİ
    private InputImage getInputImageFromFrame(Frame frame) {

        byte[] data = frame.getData();
        return InputImage.fromByteArray(data,
                frame.getSize().getWidth(),
                frame.getSize().getHeight(),
                frame.getRotationToView(),
                InputImage.IMAGE_FORMAT_NV21);

    }

    // GÖRSEL İŞLENMESİ
    private void processImage(InputImage image){
        objectDetector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<DetectedObject>>() {
                            @Override
                            public void onSuccess(List<DetectedObject> detectedObjects) {
                                processResults(detectedObjects);
                            }
                        });
    }

    private void processResults(List<DetectedObject> detectedObjects) {
        for (DetectedObject detectedObject : detectedObjects) {
            // Karelerin Silinmesi (Birikmemesi)
            if(binding.fLayout.getChildCount() > 1) {
                binding.fLayout.removeViewAt(1);
            }

            //Tespit Edilen Obje Dikdörtgeni
            Rect boundingBox = detectedObject.getBoundingBox();

            //Herhangi bir tespit olmadığında obje adı ve olasılığı
            String text = "Tanımlı Nesne Bulunamadı!";
            String confidenceText = "% 0";

            if (detectedObject.getLabels().size() != 0){
                // Tespit Edilen Obje Adı
                text = detectedObject.getLabels().get(0).getText();

                // Kategori İsmi Değiştirme
                if (PredefinedCategory.FOOD.equals(text)){
                    text = "Yiyecek";
                }

                // Olasılık
                float confidence = detectedObject.getLabels().get(0).getConfidence();
                int confidentInt = (int) (confidence * 100);
                confidenceText = "% " + Integer.toString(confidentInt);
            }

            Draw element = new Draw(this, boundingBox, text, confidenceText);
            binding.fLayout.addView(element);

        }
    }
}