package com.icanerdogan.barcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.InputStream;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button scanBtn, galleryBtn;
    CameraView cameraView;
    AlertDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = findViewById(R.id.btnScan);
        galleryBtn = findViewById(R.id.btnGallery);

        cameraView = findViewById(R.id.camera);

        scanBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);

        waitDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false)
                .build();

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();
                runScanner(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    // Barcode Detect
    private void runScanner(Bitmap bitmap) {
        int rotationDegree = 0;

        InputImage image = InputImage.fromBitmap(bitmap, rotationDegree);

        BarcodeScanner scanner = BarcodeScanning.getClient();

        scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        processScanner(barcodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Information From Barcode
    private void processScanner(List<Barcode> barcodes) {
            if (barcodes.isEmpty()){
                Toast.makeText(MainActivity.this, "Barcode Tespit Edilemedi", Toast.LENGTH_SHORT).show();
            }
            else{
                for (Barcode barcode: barcodes) {
                    String rawValue = barcode.getRawValue();

                    int valueType = barcode.getValueType();

                    switch (valueType) {
                        case Barcode.TYPE_URL:
                            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(rawValue));
                            startActivity(intent1);
                            break;
                        case Barcode.TYPE_PHONE:
                            Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse(rawValue));
                            startActivity(intent2);
                            break;
                        case Barcode.TYPE_WIFI:
                            String ssid = barcode.getWifi().getSsid();
                            String password = barcode.getWifi().getPassword();

                            String info1 = new StringBuilder("SSID: ")
                                    .append(ssid)
                                    .append("\n")
                                    .append("------------------------------------------------------")
                                    .append("\n")
                                    .append("Password: ")
                                    .append(password)
                                    .toString();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                            builder1.setMessage(info1);

                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog dialog1 = builder1.create();
                            dialog1.show();
                            break;
                        case Barcode.TYPE_CONTACT_INFO:
                            String info2 = new StringBuilder("Name: ")
                                    .append(barcode.getContactInfo().getName().getFormattedName())
                                    .append("\n")
                                    .append("------------------------------------------------------")
                                    .append("\n")
                                    .append("E-mail: ")
                                    .append(barcode.getContactInfo().getEmails().get(0).getAddress())
                                    .append("\n")
                                    .append("------------------------------------------------------")
                                    .append("\n")
                                    .append("Phone: ")
                                    .append(barcode.getContactInfo().getPhones().get(0).getNumber())
                                    .append("\n")
                                    .append("------------------------------------------------------")
                                    .append("\n")
                                    .append("Company: ")
                                    .append(barcode.getContactInfo().getOrganization())
                                    .append("\n")
                                    .append("------------------------------------------------------")
                                    .append("\n")
                                    .append("Address: ")
                                    .append(barcode.getContactInfo().getTitle())
                                    .toString();

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                            builder2.setMessage(info2);
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog dialog2 = builder2.create();
                            dialog2.show();
                            break;
                        case Barcode.TYPE_TEXT:
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(this);

                            builder3.setMessage(rawValue);

                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                            AlertDialog dialog3 = builder3.create();
                            dialog3.show();
                            break;
                        default:
                            Toast.makeText(this, "Barcode Tipi Tespit Edilemedi...",Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
            waitDialog.dismiss();
            cameraView.start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnScan:
                scanCode();
                break;
            case R.id.btnGallery:
                galleryAddPic();
                break;
        }
    }

    // Camera Function
    private void scanCode() {
        cameraView.start();
        cameraView.captureImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraView.start();
    }

    // Gallery Function
    static final int REQUEST_GALLERY = 1;
    private void galleryAddPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    // Activity Result
    Bitmap galleryImage;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK){
                    try {
                        Uri uri = data.getData();
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        galleryImage = BitmapFactory.decodeStream(inputStream);
                        runScanner(galleryImage);
                        waitDialog.show();
                    }catch (Exception e){
                        Toast.makeText(this, "Görsel Alınamadı", Toast.LENGTH_SHORT).show();
                    }

                }
            break;
        }
    }



}