package com.icanerdogan.imagelabeler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    TextView textView;
    ImageButton btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnRefresh = findViewById(R.id.refresh);
        imageView = findViewById(R.id.detectedImageView);

        textView = findViewById(R.id.detectedTextView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        btnRefresh.setOnClickListener(this);

        hideTitleBar();

        // GET INFORMATION
        Intent intent = getIntent();

        String text = intent.getStringExtra("detectText");
        textView.setText(text);

        Singleton singleton = Singleton.getInstance();
        imageView.setImageBitmap(singleton.getMyImage());

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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refresh:
                backMain();
                break;
        }
    }

    // KAMERA GÖRÜNTÜSÜNE DÖNÜŞ
    private void backMain() {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideTitleBar();
    }
}