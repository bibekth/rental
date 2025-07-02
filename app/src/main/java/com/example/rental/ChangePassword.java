package com.example.rental;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        TextView backButton = findViewById(R.id.backButton);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, ProfileActivity.class);
                startActivity(intent);
                finish(); // Optional: Finish the current activity
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the alert dialog before navigating to LoginActivity
                showPasswordChangedDialog();
            }
        });
    }

    private void showPasswordChangedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                navigateToLogin();
            }
        });

        dialog.show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
        intent.putExtra("show_toast", true);
        startActivity(intent);
        finish(); // Optional: Finish the current activity
    }
}
