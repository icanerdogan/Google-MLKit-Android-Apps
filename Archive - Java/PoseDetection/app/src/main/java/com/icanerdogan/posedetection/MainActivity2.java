package com.icanerdogan.posedetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    ImageView imageViewPose;
    LinearLayout btnRefresh;
    TextView angleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageViewPose = findViewById(R.id.imageView2);
        angleTextView = findViewById(R.id.angleText);
        btnRefresh = findViewById(R.id.refresh);

        btnRefresh.setOnClickListener(this);

        Intent intent = getIntent();
        String text = intent.getStringExtra("Text");
        angleTextView.setText(text);

        Singleton singleton = Singleton.getInstance();
        imageViewPose.setImageBitmap(singleton.getMyImage());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refresh:
                backMain();
                break;
        }
    }

    private void backMain() {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
    }
}