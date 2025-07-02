package com.example.rental;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rental.databinding.ListActivityBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Adapter.ImageAdapter;
import model.Category;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.ApiService;
import retrofit.CategoryResponse;
import retrofit.ProductResponse;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private ListActivityBinding binding;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ListActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calendar = Calendar.getInstance(); // Initialize the calendar

        // Request storage permissions
        checkAndRequestPermissions();

        binding.imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(imageUris);
        binding.imageRecyclerView.setAdapter(imageAdapter);

        binding.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        binding.addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        // Navigate back to MainActivity when the back button is clicked
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Set up the date picker dialog for purchaseYear
        binding.purchaseYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        fetchCategoriesFromBackend();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ListActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the calendar with the selected date
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Format the date in the desired order: day-month-year
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(calendar.getTime());

                        // Set the formatted date to the EditText
                        binding.purchaseYear.setText(formattedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Show the date picker dialog
        datePickerDialog.show();
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with accessing storage
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchCategoriesFromBackend() {
        ApiService apiService = RetrofitClient.getApiService(this);
        String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(getApplicationContext()));
        Call<CategoryResponse> call = apiService.getCategories(bearerToken);

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getData();
                    if (categories != null && !categories.isEmpty()) {
                        ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                                ListActivity.this,
                                R.layout.categories_spinner_item,
                                categories
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.categorySpinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(ListActivity.this, "No categories available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ListActivity.this, "Failed to load categories: Invalid response", Toast.LENGTH_SHORT).show();
                    Log.e("Category Error", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(ListActivity.this, "Failed to load categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Category Failure", "Error: " + t.getMessage());
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
            }
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void addProduct() {
        String name = binding.productName.getText().toString().trim();
        String description = binding.productDescription.getText().toString().trim();
        String amountStr = binding.price.getText().toString().trim();
        String purchaseDate = binding.purchaseYear.getText().toString().trim(); // Retrieving the purchase year

        if (name.isEmpty() || description.isEmpty() || amountStr.isEmpty() || purchaseDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // Convert strings to RequestBody
        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody amountPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(amount));
        RequestBody purchaseDatePart = RequestBody.create(MediaType.parse("text/plain"), purchaseDate);

        // Retrieve the selected Category object from the Spinner
        Category selectedCategory = (Category) binding.categorySpinner.getSelectedItem();
        RequestBody categoryIdPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedCategory.getId()));

        // Prepare the image file as MultipartBody.Part
        MultipartBody.Part body = null;
        if (!imageUris.isEmpty()) {
            Uri uri = imageUris.get(0);
            String filePath = FileUtils.getPath(this, uri);
            if (filePath != null) {
                File file = new File(filePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            } else {
                Toast.makeText(this, "Failed to process selected image", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ApiService apiService = RetrofitClient.getApiService(this);

        String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(getApplicationContext()));
        Call<ProductResponse> call = apiService.addProduct(bearerToken, namePart, descriptionPart, amountPart, categoryIdPart, purchaseDatePart, body);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ListActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    ProductResponse productResponse = response.body();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", productResponse.getData().getName());
                    resultIntent.putExtra("description", productResponse.getData().getDescription());
                    resultIntent.putExtra("amount", productResponse.getData().getAmount());
                    resultIntent.putExtra("id", productResponse.getData().getId());
                    resultIntent.putExtra("image", productResponse.getData().getPhoto());
                    resultIntent.putExtra("purchaseDate", productResponse.getData().getPurchaseDate()); // Pass the purchase date
                    resultIntent.putExtra("category", selectedCategory.getName());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(ListActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    Log.e("Product Error", "Response error: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(ListActivity.this, "Failed to add product: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Product Failure", "Error: " + t.getMessage());
            }
        });
    }
}
