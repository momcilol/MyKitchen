package com.example.mykitchen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mykitchen.R;

public class WelcomeActivity extends AppCompatActivity {

    Animation image, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        image = AnimationUtils.loadAnimation(this, R.anim.welcome_image_animation);
        title = AnimationUtils.loadAnimation(this, R.anim.welcome_title_nimation);

        findViewById(R.id.welcome_image_view).setAnimation(image);
        findViewById(R.id.welcome_text_view).setAnimation(title);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }, 5000);
    }
}