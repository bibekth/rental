package com.example.rental;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.rental.databinding.ActivityProductDetailBinding;
import com.f1soft.esewapaymentsdk.EsewaConfiguration;
import com.f1soft.esewapaymentsdk.EsewaPayment;
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit.ApiService;
import retrofit.ProductResponse;
import retrofit.RetrofitClient;
import retrofit.WishlistRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private boolean isLiked = false;
    private int productId;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the product details passed from the previous activity or fragment
        String productName = getIntent().getStringExtra("product_name");
        double productPrice = getIntent().getDoubleExtra("product_price", 0);
        String productDescription = getIntent().getStringExtra("product_description");
        String productPhoto = getIntent().getStringExtra("product_photo");
        String purchaseDate = getIntent().getStringExtra("purchase_date");
        productId = getIntent().getIntExtra("product_id", 0);

        SharedPreferences sharedPreferences = getSharedPreferences("SecondHandAppPrefs", Context.MODE_PRIVATE);
        token = "Bearer " + sharedPreferences.getString("token", null);

        // Debugging: Log the productId
        Log.d("ProductDetailActivity", "Product ID: " + productId);

        // Check if the productId is valid
        if (productId == 0) {
            Toast.makeText(this, "Invalid product ID. Unable to load product details.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the product ID is invalid
            return;
        }

        // Set product details to the UI
        binding.productName.setText(productName);
        binding.price.setText("Per Month: Rs " + productPrice);
        binding.productDescription.setText(productDescription);
        binding.purchaseDate.setText("Purchase Date: " + purchaseDate);

        // Load the product image
        String imageUrl = RetrofitClient.getProductImageUrl(productPhoto);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.productImage);

        checkWishlist();

        binding.likeButton.setOnClickListener(v -> {

            Drawable currentDrawable = binding.likeButton.getDrawable();
            Drawable likeDrawable = ContextCompat.getDrawable(this, R.drawable.ic_like);
            Drawable likeFilledDrawable = ContextCompat.getDrawable(this, R.drawable.ic_like_filled);

            if (currentDrawable != null && likeDrawable != null && likeFilledDrawable != null) {
                if (DrawableCompat.unwrap(currentDrawable).getConstantState().equals(DrawableCompat.unwrap(likeDrawable).getConstantState())) {
                    binding.likeButton.setImageResource(R.drawable.ic_like_filled);
                    addProductToWishlist(productId);
                } else {
                    binding.likeButton.setImageResource(R.drawable.ic_like);
                    removeProductFromWishlist(productId);
                }
            }
        });

        // Navigate to Feedback activity when comments_feedback is clicked
        binding.commentsFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, Feedback.class);
            intent.putExtra("postId", productId);  // Pass the productId as postId to the Feedback activity
            intent.putExtra("productName", productName);
            startActivity(intent);
        });

        // Handle Buy Now button click
        binding.buyNowButton.setOnClickListener(v -> {
//            EsewaConfiguration eSewaConfiguration = new EsewaConfiguration("JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R","BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==",EsewaConfiguration.ENVIRONMENT_TEST);
//
//            HashMap<String, String> additionalProps = new HashMap<>();
//            additionalProps.put("customKey1", "value1");
//
//            assert productName != null;
//            EsewaPayment eSewaPayment = new EsewaPayment(
//                    String.valueOf(productPrice),
//                    productName,
//                    String.valueOf(productId),
//                    "",additionalProps
//            );
//
//            Intent intent = new Intent(ProductDetailActivity.this, EsewaPaymentActivity.class);
//            intent.putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
//            intent.putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment);
//            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            try {
                Log.d("ProductDetailActivity", "Buy Now clicked. Product ID: " + productId + ", Price: " + productPrice);
                Intent intent = new Intent(ProductDetailActivity.this, OrderDetailActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("product_price", productPrice);
                intent.putExtra("product_id", productId);
                startActivity(intent);
                Log.d("ProductDetailActivity", "Navigating to OrderDetailActivity");
                finish();  // Optional: Finish the current activity to prevent it from being in the back stack
            } catch (Exception e) {
                Log.e("ProductDetailActivity", "Error starting OrderDetailActivity: " + e.getMessage());
                Toast.makeText(ProductDetailActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Chat with Seller button click
        binding.chatWithSellerButton.setOnClickListener(v -> {
//            EsewaConfiguration eSewaConfiguration = new EsewaConfiguration("JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R","BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==",EsewaConfiguration.ENVIRONMENT_TEST);
//
//            HashMap<String, String> additionalProps = new HashMap<>();
//            additionalProps.put("customKey1", "value1");
//
//            assert productName != null;
//            EsewaPayment eSewaPayment = new EsewaPayment(
//                    String.valueOf(productPrice),
//                    productName,
//                    String.valueOf(productId),
//                    "",additionalProps
//            );
//
//            Intent intent = new Intent(ProductDetailActivity.this, EsewaPaymentActivity.class);
//            intent.putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
//            intent.putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment);
//            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            try {
                Log.d("ProductDetailActivity", "Chat with Seller clicked. Product ID: " + productId + ", Price: " + productPrice);
                Intent intent = new Intent(ProductDetailActivity.this, OrderDetailActivity.class);
                intent.putExtra("product_name", productName);
                intent.putExtra("product_price", productPrice);
                intent.putExtra("product_id", productId);
                startActivity(intent);
                Log.d("ProductDetailActivity", "Navigating to OrderDetailActivity");
                finish();  // Optional: Finish the current activity to prevent it from being in the back stack
            } catch (Exception e) {
                Log.e("ProductDetailActivity", "Error starting OrderDetailActivity: " + e.getMessage());
                Toast.makeText(ProductDetailActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE_PAYMENT) {
//            if (resultCode == RESULT_OK) {
//                String message = data.getStringExtra("message");
//                Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    private void addProductToWishlist(int productId) {
        Log.d("ProductDetailActivity", "Attempting to add product to wishlist. Product ID: " + productId);
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

        apiService.addToWishlist(token ,productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    Log.d("ProductDetailActivity", "Product added to wishlist successfully.");
                    sendWishlistUpdateBroadcast(true); // Notify SavedItemsFragment about the addition
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to add to wishlist", Toast.LENGTH_SHORT).show();
                    Log.d("ProductDetailActivity", "Failed to add to wishlist: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                Log.e("ProductDetailActivity", "Failed to add product to wishlist. Error: " + t.getMessage());
            }
        });
    }

    private void removeProductFromWishlist(int productId) {
        Log.d("ProductDetailActivity", "Attempting to remove product from wishlist. Product ID: " + productId);
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(getApplicationContext()));
        apiService.removeFromWishlist(bearerToken, productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                    Log.d("ProductDetailActivity", "Product removed from wishlist successfully.");
                    sendWishlistUpdateBroadcast(false); // Notify SavedItemsFragment about the removal
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to remove from wishlist", Toast.LENGTH_SHORT).show();
                    Log.d("ProductDetailActivity", "Failed to remove from wishlist: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                Log.e("ProductDetailActivity", "Failed to remove product from wishlist. Error: " + t.getMessage());
            }
        });
    }

    private void sendWishlistUpdateBroadcast(boolean added) {
        Log.d("ProductDetailActivity", "Sending broadcast. Product ID: " + productId + ", Added: " + added);
        Intent intent = new Intent("wishlist-updated");
        intent.putExtra("product_id", productId);
        intent.putExtra("added", added);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void checkWishlist(){
        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
        Call<ProductResponse> call  = apiService.checkWishlist(token, productId);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().isLiked()){
                        binding.likeButton.setImageResource(R.drawable.ic_like_filled);
                    }else{
                        binding.likeButton.setImageResource(R.drawable.ic_like);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });
    }
}
