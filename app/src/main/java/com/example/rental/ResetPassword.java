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
import retrofit.OtpVerificationRequest;
import retrofit.ResetPasswordRequest;
import retrofit.OtpVerificationResponse;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText otpCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordET);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        otpCodeEditText = findViewById(R.id.otpCodeEditText);

        findViewById(R.id.resetPasswordButton).setOnClickListener(v -> resetPassword());

        findViewById(R.id.resendotpButton).setOnClickListener(v -> resendOtp());
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String otpCode = otpCodeEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || otpCode.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        ResetPasswordRequest request = new ResetPasswordRequest(email, otpCode, password);

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<OtpVerificationResponse> call = apiService.resetPassword(request);

        call.enqueue(new Callback<OtpVerificationResponse>() {
            @Override
            public void onResponse(Call<OtpVerificationResponse> call, Response<OtpVerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(ResetPassword.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                        navigateToLogin();
                    } else {
                        // Convert List<String> to a single String
                        List<String> messages = response.body().getMessage();
                        String message = messages != null && !messages.isEmpty() ? TextUtils.join("\n", messages) : "An unknown error occurred.";
                        Toast.makeText(ResetPassword.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle error case
                    String errorMessage = "An error occurred. Please try again.";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(ResetPassword.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpVerificationResponse> call, Throwable t) {
                Toast.makeText(ResetPassword.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("ResetPassword", "onFailure: ", t);
            }
        });
    }

    private void resendOtp() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        OtpVerificationRequest request = new OtpVerificationRequest(email, null);

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<OtpVerificationResponse> call = apiService.resendResetPasswordOtp(request);

        call.enqueue(new Callback<OtpVerificationResponse>() {
            @Override
            public void onResponse(Call<OtpVerificationResponse> call, Response<OtpVerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ResetPassword.this, "OTP resent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPassword.this, "Failed to resend OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpVerificationResponse> call, Throwable t) {
                Toast.makeText(ResetPassword.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
