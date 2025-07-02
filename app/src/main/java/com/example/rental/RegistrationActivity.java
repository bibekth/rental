package com.example.rental;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit.RegistrationRequest;
import retrofit.RegistrationResponse;
import retrofit.ApiService;
import retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SecondHandAppPrefs";
    private static final String PREF_EMAIL = "email";

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText reEnterPasswordEditText;
    private EditText phoneNumberEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        reEnterPasswordEditText = findViewById(R.id.reEnterPasswordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        TextView loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(v -> startLoginActivity());

        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(v -> register());

        TextView cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> startLoginActivity());
    }

    private void register() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String reEnterPassword = reEnterPasswordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || reEnterPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(reEnterPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService(this);
        RegistrationRequest registrationRequest = new RegistrationRequest(fullName, email, password, phoneNumber);

        Log.d("RegistrationActivity", "Registration request: " + registrationRequest.toString());

        Call<RegistrationResponse> call = apiService.register(registrationRequest);

        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RegistrationActivity", "Registration successful: " + response.body().toString());

                    // Save email locally using SharedPreferences
                    saveEmailLocally(email);

                    // Assuming the ID is returned in the response
                    String userId = response.body().getUserId(); // Update based on your API response structure
                    Log.d("RegistrationActivity", "User ID: " + userId);

                    // Show success message and move to next activity
                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    // Pass the user ID and email to the next activity
                    startRegistrationOTPActivity(userId);
                } else {
                    handleRegistrationFailure(response);
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("RegistrationActivity", "onFailure: ", t);
            }
        });
    }

    private void saveEmailLocally(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_EMAIL, email);
        editor.apply();
    }

    private void handleRegistrationFailure(Response<RegistrationResponse> response) {
        Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                Log.e("RegistrationActivity", "Error body: " + errorBody);

                // Parse and display a meaningful error message
                String errorMessage = extractErrorMessage(errorBody);
                Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(RegistrationActivity.this, "Error processing response", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("RegistrationActivity", "Error response: " + response.message());
            Toast.makeText(RegistrationActivity.this, "Registration Failed: " + response.message(), Toast.LENGTH_LONG).show();
        }
    }

    private String extractErrorMessage(String errorBody) {
        // This method should parse the errorBody and return a concise error message
        // Assuming the Laravel API returns errors in JSON format
        try {
            JSONObject jsonObject = new JSONObject(errorBody);
            if (jsonObject.has("message")) {
                return jsonObject.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "An unknown error occurred";
    }

    public void startLoginActivity() {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void startRegistrationOTPActivity(String userId) {
        Intent intent = new Intent(RegistrationActivity.this, RegistrationOTPActivity.class);
        intent.putExtra("user_id", userId); // Pass the user ID to the OTP Activity
        startActivity(intent);
        finish();
    }
}
