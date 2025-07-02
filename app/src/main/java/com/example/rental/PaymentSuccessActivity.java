package com.example.rental;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        ImageView closeButton = findViewById(R.id.close_button);
        Button doneButton = findViewById(R.id.done_button);

        // Close the success activity and return to OrderDetailActivity when the close button is clicked
        closeButton.setOnClickListener(v -> {
            finish();  // This will take the user back to OrderDetailActivity
        });

        // Navigate to MainActivity when the done button is clicked
        doneButton.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();  // This ensures that the PaymentSuccessActivity is removed from the back stack
        });
    }
}
