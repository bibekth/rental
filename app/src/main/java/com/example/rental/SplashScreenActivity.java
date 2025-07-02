package com.example.rental;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SecondHandAppPrefs";
    private static final String PREF_TOKEN = "token"; // The key used to store the token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ImageView logo = findViewById(R.id.circleImageView);

        // Create fade-in animation for logo alpha
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        fadeIn.setDuration(500); // duration 2 seconds

        // Create rotation animation for logo
        ObjectAnimator rotate = ObjectAnimator.ofFloat(logo, "rotation", 0f, 360f);
        rotate.setDuration(500); // duration 1 second

        // Create an AnimatorSet to play both animations together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fadeIn, rotate);
        animatorSet.start();

        // Delay to allow animations to finish before checking the token
        new Handler().postDelayed(() -> {
            checkTokenAndNavigate();
        }, 500); // 2 seconds delay
    }

    private void checkTokenAndNavigate() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(PREF_TOKEN, null);

        if (token != null) {
            // Token exists, navigate to MainActivity
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // Token does not exist, navigate to LoginActivity
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish(); // Finish the SplashScreenActivity so it's not in the back stack
    }
}
