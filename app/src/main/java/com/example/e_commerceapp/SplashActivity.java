package com.example.e_commerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500; // 2.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide action bar for fullscreen effect
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        // Hide status bar
        getWindow().getDecorView().setSystemUiVisibility(
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN |
            android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // Navigate to LoginActivity after delay
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, Login.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}