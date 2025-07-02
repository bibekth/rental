package com.example.rental;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class dialog_reset_successful extends AppCompatActivity {

    private LoadingAlert loadingAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_reset_successful);

        // Initialize the loading alert
        loadingAlert = new LoadingAlert(this);

        Button loginDialogButton = findViewById(R.id.logindialogButton);
        ViewCompat.setOnApplyWindowInsetsListener(loginDialogButton, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginDialogButton.setOnClickListener(v -> {
            // Show the loading dialog
            loadingAlert.setMessage("Resetting Password...");
            loadingAlert.show();

            // Simulate a delay to show the loading dialog
            new Handler().postDelayed(() -> {
                // Dismiss the loading dialog
                loadingAlert.dismiss();

                // Navigate to LoginActivity
                Intent intent = new Intent(dialog_reset_successful.this, LoginActivity.class);
                intent.putExtra("show_toast", true);
                startActivity(intent);
                finish();
            }, 2000); // 2 seconds delay
        });
    }
}
