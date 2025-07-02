package com.example.rental;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import retrofit.ApiService;
import retrofit.OtpVerificationRequest;
import retrofit.OtpVerificationResponse;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class RegistrationOTPActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SecondHandAppPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_TOKEN = "token"; // Assuming you have a token stored

    private EditText otpEditText1, otpEditText2, otpEditText3, otpEditText4, otpEditText5, otpEditText6;
    private TextView resendOtpTextView, cancelButton;
    private Button verifyOtpButton;
    private MaterialButton resendOtpButton;
    private CountDownTimer countDownTimer;

    private String userEmail;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_otp_activity);

        // Retrieve email and token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString(PREF_EMAIL, null);
        userToken = sharedPreferences.getString(PREF_TOKEN, null);

        if (userEmail == null) {
            Toast.makeText(this, "No email found, please register again.", Toast.LENGTH_SHORT).show();
            navigateToRegistrationActivity();
            return;
        }

        otpEditText1 = findViewById(R.id.otpEditText1);
        otpEditText2 = findViewById(R.id.otpEditText2);
        otpEditText3 = findViewById(R.id.otpEditText3);
        otpEditText4 = findViewById(R.id.otpEditText4);
        otpEditText5 = findViewById(R.id.otpEditText5);
        otpEditText6 = findViewById(R.id.otpEditText6);
        resendOtpTextView = findViewById(R.id.resendOtpTextView);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);
        cancelButton = findViewById(R.id.cancelButton);
        resendOtpButton = findViewById(R.id.resendOtpButton);

        startCountdownTimer();
        setupOtpEditTexts();

        verifyOtpButton.setOnClickListener(v -> {
            String otp = otpEditText1.getText().toString() +
                    otpEditText2.getText().toString() +
                    otpEditText3.getText().toString() +
                    otpEditText4.getText().toString() +
                    otpEditText5.getText().toString() +
                    otpEditText6.getText().toString();

            if (otp.length() == 6) {
                verifyOtp(otp);
            } else {
                Toast.makeText(this, "Please enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationOTPActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        resendOtpButton.setOnClickListener(v -> {
            resendOtp();
            startCountdownTimer();
        });
    }

    private void setupOtpEditTexts() {
        otpEditText1.addTextChangedListener(new GenericTextWatcher(otpEditText1, otpEditText2));
        otpEditText2.addTextChangedListener(new GenericTextWatcher(otpEditText2, otpEditText3));
        otpEditText3.addTextChangedListener(new GenericTextWatcher(otpEditText3, otpEditText4));
        otpEditText4.addTextChangedListener(new GenericTextWatcher(otpEditText4, otpEditText5));
        otpEditText5.addTextChangedListener(new GenericTextWatcher(otpEditText5, otpEditText6));
        otpEditText6.addTextChangedListener(new GenericTextWatcher(otpEditText6, null));
    }

    private class GenericTextWatcher implements TextWatcher {

        private final EditText currentView;
        private final EditText nextView;

        public GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            } else if (s.length() == 0 && currentView != otpEditText1) {
                currentView.requestFocus();
            }
        }
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(95000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendOtpTextView.setText("Resend OTP in " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                resendOtpTextView.setText("You can resend the OTP now");
                resendOtpButton.setEnabled(true);
            }
        }.start();
    }

    private void resendOtp() {
        ApiService apiService = RetrofitClient.getApiService(this);
        OtpVerificationRequest request = new OtpVerificationRequest(userEmail, userToken);

        Log.d("RegistrationOTPActivity", "Resend OTP request: " + request);

        Call<OtpVerificationResponse> call = apiService.resendOtp(request);

        call.enqueue(new Callback<OtpVerificationResponse>() {
            @Override
            public void onResponse(Call<OtpVerificationResponse> call, Response<OtpVerificationResponse> response) {
                Log.d("RegistrationOTPActivity", "Response code: " + response.code());
                Log.d("RegistrationOTPActivity", "Response message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(RegistrationOTPActivity.this, "OTP resent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        List<String> messages = response.body().getMessage();
                        String errorMessage = messages != null && !messages.isEmpty() ? messages.get(0) : "An unknown error occurred.";
                        Log.e("RegistrationOTPActivity", "Resend OTP failed: " + errorMessage);
                        Toast.makeText(RegistrationOTPActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage;
                    try {
                        errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("RegistrationOTPActivity", "Resend OTP error body: " + errorMessage);
                    } catch (IOException e) {
                        errorMessage = "Error parsing error response";
                        Log.e("RegistrationOTPActivity", "IOException while parsing error body", e);
                    }

                    Toast.makeText(RegistrationOTPActivity.this, "Failed to resend OTP. " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpVerificationResponse> call, Throwable t) {
                Log.e("RegistrationOTPActivity", "onFailure: ", t);
                Toast.makeText(RegistrationOTPActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOtp(String otp) {
        ApiService apiService = RetrofitClient.getApiService(this);
        OtpVerificationRequest otpVerificationRequest = new OtpVerificationRequest(userEmail, otp);

        Log.d("RegistrationOTPActivity", "otp is " + otp);
        Log.d("RegistrationOTPActivity", "otpVerificationRequest: " + otpVerificationRequest);

        Call<OtpVerificationResponse> call = apiService.verifyOtp(otpVerificationRequest);
        call.enqueue(new Callback<OtpVerificationResponse>() {
            @Override
            public void onResponse(Call<OtpVerificationResponse> call, Response<OtpVerificationResponse> response) {
                Log.d("RegistrationOTPActivity", "Response code: " + response.code() + " Message: " + response.message());

                if (response.isSuccessful() && response.body() != null) {
                    OtpVerificationResponse otpResponse = response.body();
                    if (otpResponse.isSuccess()) {
                        String token = otpResponse.getToken();
                        Log.d("RegistrationOTPActivity", "OTP Verified. Token: " + token);
                        navigateToLoginActivity(token);
                    } else {
                        List<String> messages = otpResponse.getMessage();
                        String errorMessage = messages != null && !messages.isEmpty() ? messages.get(0) : "An unknown error occurred.";
                        Log.e("RegistrationOTPActivity", "OTP verification failed: " + errorMessage);
                        Toast.makeText(RegistrationOTPActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("RegistrationOTPActivity", "Error body: " + errorBody);
                        Toast.makeText(RegistrationOTPActivity.this, "OTP Verification Failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(RegistrationOTPActivity.this, "OTP Verification Failed. Response is not successful or body is null.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OtpVerificationResponse> call, Throwable t) {
                Toast.makeText(RegistrationOTPActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("RegistrationOTPActivity", "onFailure: ", t);
            }
        });
    }

    private void navigateToLoginActivity(String token) {
        Intent intent = new Intent(RegistrationOTPActivity.this, LoginActivity.class);
        intent.putExtra("email", userEmail); // Optionally pass the email to LoginActivity
        if (token != null) {
            intent.putExtra("token", token); // Pass the token to LoginActivity if available
        }
        startActivity(intent);
        finish();
    }

    private void navigateToRegistrationActivity() {
        Intent intent = new Intent(RegistrationOTPActivity.this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}
