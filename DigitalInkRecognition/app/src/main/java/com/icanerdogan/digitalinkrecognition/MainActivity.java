package com.icanerdogan.digitalinkrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btnRecognize, btnClear;
    DrawView drawView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideTitleBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecognize = findViewById(R.id.buttonRecognize);
        btnClear = findViewById(R.id.buttonClear);
        drawView = findViewById(R.id.draw_view);
        textView = findViewById(R.id.textResult);

        StrokeManager.download();

        btnRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrokeManager.recognize(MainActivity.this, textView);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.clear();
                StrokeManager.clear();
                textView.setText("");
            }
        });
    }
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

    @Override
    protected void onPause() {
        super.onPause();
        hideTitleBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideTitleBar();
    }
}