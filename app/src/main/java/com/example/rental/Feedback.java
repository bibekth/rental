package com.example.rental;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rental.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.FeedbackAdapter;
import okhttp3.ResponseBody;
import retrofit.ApiService;
import retrofit.CommentRequest;
import retrofit.CommentListResponse;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Feedback extends AppCompatActivity {

    private static final String TAG = "Feedback";
    private RecyclerView commentsRecyclerView;
    private static final String PREFS_NAME = "SecondHandAppPrefs";
    private static final String PREF_TOKEN = "token";
    private EditText commentEditText;
    private Button sendButton;
    private TextView productNameTextView;
    private ImageButton backButton;

    private FeedbackAdapter feedbackAdapter;
    private List<CommentListResponse.Data> commentsList = new ArrayList<>();
    private int productId;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize views
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentEditText = findViewById(R.id.commentEditText);
        sendButton = findViewById(R.id.sendButton);
        productNameTextView = findViewById(R.id.productNameTextView);
        backButton = findViewById(R.id.backButton);

        // Retrieve product details from the intent
        productId = getIntent().getIntExtra("postId", -1);
        String productName = getIntent().getStringExtra("productName");

        // Debugging: Log the productId
        Log.d(TAG, "Received Product ID: " + productId);

        // Check if the product ID is valid
        if (productId == -1 || productName == null || productId == 0) {
            Toast.makeText(this, "Failed to fetch product details. Invalid product ID.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if productId is invalid
            return;
        }

        // Set the product name in the TextView
        productNameTextView.setText(productName);

        // Set up the RecyclerView and adapter
        feedbackAdapter = new FeedbackAdapter(commentsList, new FeedbackAdapter.OnCommentInteractionListener() {
            @Override
            public void onEditClick(CommentListResponse.Data comment) {
                Toast.makeText(Feedback.this, "Edit Comment: " + comment.getContent(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(CommentListResponse.Data comment) {
                deleteComment(comment.getId());
            }

            public int getUserId() {
                return -1;  // Customize this to return the actual user ID if needed
            }
        });

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(feedbackAdapter);

        // Fetch comments from the backend
        fetchComments();

        // Set up the send button to post a comment
        sendButton.setOnClickListener(v -> postComment());

        // Set up back button to navigate to ProductDetailActivity
        backButton.setOnClickListener(v -> {
            // Go back to ProductDetailActivity
            Intent intent = new Intent(Feedback.this, ProductDetailActivity.class);
            intent.putExtra("productId", productId); // Pass any needed data
            startActivity(intent);
            finish(); // Finish the current activity
        });
    }

    private void fetchComments() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(PREF_TOKEN, null);

        if (token == null) {
            Toast.makeText(this, "Token is missing. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Fetching comments for Product ID: " + productId);  // Log the productId before making the request

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<CommentListResponse> call = apiService.getComments("Bearer " + token, productId);

        call.enqueue(new Callback<CommentListResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                if (response.isSuccessful()) {
                    CommentListResponse commentListResponse = response.body();
                    if (commentListResponse != null && commentListResponse.getData() != null) {
                        Log.d(TAG, "Number of comments fetched: " + commentListResponse.getData().size());
                        commentsList.clear();
                        commentsList.addAll(commentListResponse.getData());
                        feedbackAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Feedback.this, "No comments found", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "No comments found for product ID: " + productId);
                    }
                } else {
                    Log.e(TAG, "Failed to fetch comments. Response code: " + response.code());
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
//                Toast.makeText(Feedback.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Error fetching comments: ", t);
            }
        });
    }

    private void postComment() {
        String content = commentEditText.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(PREF_TOKEN, null);

        if (token == null) {
            Toast.makeText(this, "Token is missing. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Posting Comment with Product ID: " + productId + " and Content: " + content);

        CommentRequest commentRequest = new CommentRequest(productId, content);

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<ResponseBody> call = apiService.postComment("Bearer " + token, commentRequest);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Feedback.this, "Comment posted successfully", Toast.LENGTH_SHORT).show();
                    commentEditText.setText(""); // Clear the comment input field
                    fetchComments();  // Refresh the comments list
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Feedback.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: ", t);
            }
        });
    }

    private void deleteComment(int commentId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(PREF_TOKEN, null);

        if (token == null) {
            Toast.makeText(this, "Token is missing. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<ResponseBody> call = apiService.deleteComment("Bearer " + token, commentId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Feedback.this, "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchComments();  // Refresh the comments list
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Feedback.this, "Failed to delete comment", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: ", t);
            }
        });
    }

    private void handleErrorResponse(Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
            Log.e(TAG, "Error response: " + errorBody);
//            Toast.makeText(this, "Failed to load comments: " + errorBody, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error reading error body", e);
//            Toast.makeText(this, "Failed to load comments", Toast.LENGTH_SHORT).show();
        }
    }
}
