package com.example.rental;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.LoginRequest;
import retrofit.LoginResponse;
import retrofit.ApiService;
import retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private LoadingAlert loadingAlert;

    private static final String PREFS_NAME = "SecondHandAppPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_NAME = "name";
    private static final String PREF_PHONENUMBER = "phonenumber";
    private static final String PREF_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUIComponents();

        loadingAlert = new LoadingAlert(this);

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("show_toast", false)) {
            Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
        }

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> login());

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(PREF_TOKEN, null);
        if(token != null){
            navigateToMainActivity();
        }
    }

    private void initializeUIComponents() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        TextView registerPromptTextView = findViewById(R.id.registerPromptTextView);
        TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        registerPromptTextView.setOnClickListener(v -> startRegistrationActivity());
        forgotPasswordTextView.setOnClickListener(v -> startForgotPasswordActivity());

    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!isEmailValid(email) || !areFieldsNotEmpty(email, password)) {
            return;
        }

        loadingAlert.setMessage("Checking credentials...");
        loadingAlert.show();

        ApiService apiService = RetrofitClient.getApiService(this);
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loadingAlert.dismiss();
                handleLoginResponse(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loadingAlert.dismiss();
                handleNetworkFailure(t);
            }
        });
    }

    private boolean isEmailValid(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean areFieldsNotEmpty(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void handleLoginResponse(Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = response.body();
            if (loginResponse != null) {
                saveLoginDataLocally(
                        loginResponse.getData().getUser().getId(),
                        loginResponse.getData().getUser().getName(),
                        loginResponse.getData().getUser().getEmail(),
                        loginResponse.getData().getUser().getPhonenumber(),
                        loginResponse.getData().getToken()
                );
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            } else {
                Toast.makeText(LoginActivity.this, "Login failed: No response from server", Toast.LENGTH_SHORT).show();
            }
        } else {
            handleLoginFailure(response);
        }
    }

    private void handleNetworkFailure(Throwable t) {
        Toast.makeText(LoginActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
        Log.e("LoginActivity", "onFailure: ", t);
    }

    private void handleLoginFailure(Response<LoginResponse> response) {
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                String errorMessage = extractErrorMessage(errorBody);

                if (errorMessage.contains("Verify your email")) {
                    Toast.makeText(LoginActivity.this, "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "Error processing response", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
        }
    }

    private String extractErrorMessage(String errorBody) {
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

    private void saveLoginDataLocally(int userId, String name, String email, String phonenumber, String token) {
        RetrofitClient.saveToken(getApplicationContext(), token);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_USER_ID, userId);
        editor.putString(PREF_NAME, name);
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_PHONENUMBER, phonenumber);
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public void startRegistrationActivity() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void startForgotPasswordActivity() {
        Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
        startActivity(intent);
    }

    public void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
