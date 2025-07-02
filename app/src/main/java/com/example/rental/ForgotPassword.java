package com.example.rental;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import retrofit.ApiService;
import retrofit.ForgotPasswordRequest;
import retrofit.OtpVerificationResponse;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.emailEditText);

        findViewById(R.id.resetPasswordButton).setOnClickListener(v -> sendResetPasswordOtp());

        findViewById(R.id.cancelButton).setOnClickListener(v -> navigateToLogin());
    }

    private void sendResetPasswordOtp() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log the email before sending
        Log.d("ForgotPassword", "Sending email: " + email);

        ApiService apiService = RetrofitClient.getApiService(this);
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);

        Call<OtpVerificationResponse> call = apiService.forgotPassword(request);

        call.enqueue(new Callback<OtpVerificationResponse>() {
            @Override
            public void onResponse(Call<OtpVerificationResponse> call, Response<OtpVerificationResponse> response) {
                Log.d("ForgotPassword", "Response code: " + response.code());
                Log.d("ForgotPassword", "Response message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ForgotPassword", "Response body: " + response.body().toString());
                    if (response.body().isSuccess()) {
                        Toast.makeText(ForgotPassword.this, "OTP sent to your email", Toast.LENGTH_SHORT).show();
                        navigateToResetPassword();
                    } else {
                        List<String> messages = response.body().getMessage();
                        String message = messages != null && !messages.isEmpty() ? TextUtils.join("\n", messages) : "The operation could not be completed. Please check your email address and try again.";
                        Log.d("ForgotPassword", "Failure reason: " + message);
                        Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "An error occurred. Please try again.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                            Log.d("ForgotPassword", "Error Body: " + errorMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(ForgotPassword.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpVerificationResponse> call, Throwable t) {
                Toast.makeText(ForgotPassword.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("ForgotPassword", "onFailure: ", t);
            }
        });
    }

    private void navigateToResetPassword() {
        Intent intent = new Intent(ForgotPassword.this, ResetPassword.class);
        intent.putExtra("email", emailEditText.getText().toString().trim());
        startActivity(intent);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
        startActivity(intent);
    }
}
