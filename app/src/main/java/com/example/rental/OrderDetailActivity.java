package com.example.rental;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.f1soft.esewapaymentsdk.EsewaConfiguration;
import com.f1soft.esewapaymentsdk.EsewaPayment;
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity;

import java.util.HashMap;
import java.util.Objects;

import model.Product;
import retrofit.ApiService;
import retrofit.OrderDetailRequest;
import retrofit.OrderDetailResponse;
import retrofit.OrderRequest;
import retrofit.OrderResponse;
import retrofit.ProductResponse;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderDetailActivity extends AppCompatActivity {

    private static final String TAG = "OrderDetailActivity";

    private EditText mobileNumberEditText, deliveryAddressEditText, paymentMethodEditText;
    private TextView grandTotalAmount, tvRate;
    private Button submitOrderButton;
    private ImageView backButton;
    private int productId;
    double productPrice, total_amount;
    private static final int REQUEST_CODE_PAYMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Initialize UI elements
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
//        deliveryAddressEditText = findViewById(R.id.deliveryAddressEditText);
//        paymentMethodEditText = findViewById(R.id.paymentMethodEditText);
        tvRate = findViewById(R.id.tvRate);
        grandTotalAmount = findViewById(R.id.grandTotalAmount);
        submitOrderButton = findViewById(R.id.submitOrderButton);
        backButton = findViewById(R.id.backButton);

        // Get the product details passed from ProductDetailActivity
        String productName = getIntent().getStringExtra("product_name");
        productPrice = getIntent().getDoubleExtra("product_price", 0);
        productId = getIntent().getIntExtra("product_id", 0);

        if (productId == 0) {
            Toast.makeText(this, "Invalid product ID. Unable to proceed with the order.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvRate.setText("Rate per Month: Rs " + productPrice);
        // Set the total amount in the TextView
        grandTotalAmount.setText("Rs. " + productPrice);

        // Set back button action to navigate back to ProductDetailActivity
        backButton.setOnClickListener(v -> finish());

        submitOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> additionalProps = new HashMap<>();
                additionalProps.put("product_id", String.valueOf(productId));

                EsewaConfiguration eSewaConfiguration = new EsewaConfiguration("JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R","BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==",EsewaConfiguration.ENVIRONMENT_TEST);

                assert productName != null;
                EsewaPayment eSewaPayment = new EsewaPayment(
                        String.valueOf(total_amount),
                        productName,
                        String.valueOf(productId),
                        "",additionalProps
                );

                Intent intent = new Intent(OrderDetailActivity.this, EsewaPaymentActivity.class);
                intent.putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
                intent.putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }
        });
        textChange();
        // Set submit button action
//        submitOrderButton.setOnClickListener(v -> submitOrderDetails(productPrice, productId));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(OrderDetailActivity.this, "Payment Successful", Toast.LENGTH_LONG).show();

                ApiService apiService = RetrofitClient.getApiService(this);
                String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(getApplicationContext()));
                Call<ProductResponse> rentProduct = apiService.rentProduct(bearerToken, productId, mobileNumberEditText.getText().toString());

                rentProduct.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {

                    }
                });

                Intent mainActivity = new Intent(OrderDetailActivity.this, MainActivity.class);
                startActivity(mainActivity);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void textChange() {
        mobileNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                total_amount = productPrice * Integer.parseInt(s.toString());
                grandTotalAmount.setText("Rs. " + total_amount);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void submitOrderDetails(double grandTotalValue, int productId) {
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String deliveryAddress = deliveryAddressEditText.getText().toString().trim();
        String paymentMethod = paymentMethodEditText.getText().toString().trim();

        // Validate inputs
        if (mobileNumber.isEmpty() || deliveryAddress.isEmpty() || paymentMethod.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = 1; // Assuming quantity is 1 for simplicity
        String orderDate = "2024-08-24"; // This should be dynamic based on your requirements

        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SecondHandAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null); // Retrieve the auth token

        // Create an OrderRequest object
        OrderRequest orderRequest = new OrderRequest(String.valueOf(productId), String.valueOf(quantity), orderDate);

        // Send the order to the backend
        ApiService apiService = RetrofitClient.getApiService(this);

        Call<OrderResponse> callOrder = apiService.submitOrder("Bearer "+token, orderRequest);
        callOrder.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {

                        // Assuming order ID is returned from the response (you need to extract it)
                        int orderId = response.body().getData().getId();  // Replace with actual logic to extract order ID

                        // Now submit order details
                        submitOrderDetailsToBackend(orderId, deliveryAddress, paymentMethod, mobileNumber);

                    } catch (Exception e) {
                        Toast.makeText(OrderDetailActivity.this, "Failed to read server response.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Product is Already sold.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "An error occurred while placing the order.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitOrderDetailsToBackend(int orderId, String deliveryAddress, String paymentMethod, String mobileNumber) {
        ApiService apiService = RetrofitClient.getApiService(this);
        OrderDetailRequest orderDetailRequest = new OrderDetailRequest(orderId,
                deliveryAddress,
                paymentMethod,
                mobileNumber,
                0.0, // Assuming discount is 0.0
                true, // Assuming payment status is true
                true // Assuming delivery status is true)
        );

        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SecondHandAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null); // Retrieve the auth token



        Call<OrderDetailResponse> callOrderDetail = apiService.submitOrderDetail("Bearer "+token, orderDetailRequest);

        callOrderDetail.enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(OrderDetailActivity.this, "Order details submitted successfully.", Toast.LENGTH_SHORT).show();

                    // Navigate to the Payment Success page
                    Intent intent = new Intent(OrderDetailActivity.this, PaymentSuccessActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Failed to submit order details.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "An error occurred while submitting order details.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
