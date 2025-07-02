package com.example.rental;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import model.Category;
import retrofit.ApiService;
import retrofit.CategoryResponse;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterCardView extends LinearLayout {

    private EditText searchTermEditText, minPriceEditText, maxPriceEditText;
    private Spinner categorySpinner;
    private Button searchButton, resetButton;
    private ImageView closeFilterImageView;

    public FilterCardView(Context context) {
        super(context);
        init(context);
    }

    public FilterCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.filter_card_view, this, true);

        searchTermEditText = findViewById(R.id.search_term);
        minPriceEditText = findViewById(R.id.min_price);
        maxPriceEditText = findViewById(R.id.max_price);
        categorySpinner = findViewById(R.id.category_spinner);
        searchButton = findViewById(R.id.search_button);
        resetButton = findViewById(R.id.reset_button);
        closeFilterImageView = findViewById(R.id.close_filter);

        closeFilterImageView.setOnClickListener(v -> setVisibility(GONE));

        searchButton.setOnClickListener(v -> {
            String searchTerm = searchTermEditText.getText().toString();
            String category = categorySpinner.getSelectedItem().toString();
            String minPrice = minPriceEditText.getText().toString();
            String maxPrice = maxPriceEditText.getText().toString();

            // Handle search functionality
            Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show();
        });

        resetButton.setOnClickListener(v -> {
            searchTermEditText.setText("");
            minPriceEditText.setText("");
            maxPriceEditText.setText("");
            categorySpinner.setSelection(0);
        });

        fetchCategoriesFromBackend(context);
    }

    private void fetchCategoriesFromBackend(Context context) {
        ApiService apiService = RetrofitClient.getApiService(context);
        String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(getContext()));
        Call<CategoryResponse> call = apiService.getCategories(bearerToken);

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getData();
                    populateCategorySpinner(context, categories);
                } else {
                    Toast.makeText(context, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateCategorySpinner(Context context, List<Category> categories) {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }
}
