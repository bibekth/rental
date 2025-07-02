package com.example.rental;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapter.ProductAdapter;
import model.Category;
import model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit.ApiService;
import retrofit.CategoryResponse;
import retrofit.RetrofitClient;

public class SearchedItemActivity extends AppCompatActivity {

    private RecyclerView searchedProductList;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products;
    private View filterCardView;
    private Animation slideUp, slideDown;
    private EditText searchTerm, minPrice, maxPrice;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_item);

        // Initialize views
        EditText searchBar = findViewById(R.id.search_bar);
        ImageView messageIcon = findViewById(R.id.message_icon);
        ImageView filterIcon = findViewById(R.id.filter_icon);
        filterCardView = findViewById(R.id.filter_card_view);
        searchTerm = findViewById(R.id.search_term);
        minPrice = findViewById(R.id.min_price);
        maxPrice = findViewById(R.id.max_price);
        categorySpinner = findViewById(R.id.category_spinner);
        Button searchButton = findViewById(R.id.search_button);
        Button resetButton = findViewById(R.id.reset_button);

        // Load animations
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        // Set click listener for message icon
        messageIcon.setOnClickListener(v -> {
            Intent intent = new Intent(SearchedItemActivity.this, MessageActivity.class);
            startActivity(intent);
        });

        // Show filter card view when filter icon is clicked
        filterIcon.setOnClickListener(v -> {
            if (filterCardView.getVisibility() == View.GONE) {
                filterCardView.startAnimation(slideUp);
                filterCardView.setVisibility(View.VISIBLE);
            } else {
                filterCardView.startAnimation(slideDown);
                filterCardView.setVisibility(View.GONE);
            }
        });

        // Handle close filter button
        ImageView closeFilter = filterCardView.findViewById(R.id.close_filter);
        closeFilter.setOnClickListener(v -> {
            filterCardView.startAnimation(slideDown);
            filterCardView.setVisibility(View.GONE);
        });

        // Handle search action from the keyboard
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch(searchBar.getText().toString());
                return true;
            }
            return false;
        });

        // Get the selected category ID and product list from the intent
        int selectedCategoryId = getIntent().getIntExtra("selected_category_id", -1);
        products = getIntent().getParcelableArrayListExtra("product_list");

        // Filter products based on selected category
        ArrayList<Product> filteredProducts = filterProductsByCategory(products, selectedCategoryId);

        // Set up RecyclerView
        searchedProductList = findViewById(R.id.searched_product_list);
        productAdapter = new ProductAdapter(this, filteredProducts);
        searchedProductList.setLayoutManager(new GridLayoutManager(this, 3)); // Set span count to 3
        searchedProductList.setAdapter(productAdapter);

        // Load categories into the Spinner
        loadCategories();

        // Handle search button click in filter card view
        searchButton.setOnClickListener(v -> {
            String term = searchTerm.getText().toString().toLowerCase();
            String category = categorySpinner.getSelectedItem().toString();
            double min = minPrice.getText().toString().isEmpty() ? 0 : Double.parseDouble(minPrice.getText().toString());
            double max = maxPrice.getText().toString().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPrice.getText().toString());

            ArrayList<Product> filteredList = filterProducts(term, category, min, max);
            productAdapter.updateList(filteredList);
            filterCardView.startAnimation(slideDown);
            filterCardView.setVisibility(View.GONE);
        });

        // Handle reset button click in filter card view
        resetButton.setOnClickListener(v -> resetFilters());
    }

    private ArrayList<Product> filterProductsByCategory(ArrayList<Product> products, int categoryId) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().getId() == categoryId) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    private ArrayList<Product> filterProducts(String term, String category, double min, double max) {
        ArrayList<Product> filteredList = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(term)
                    && product.getCategory().getName().equals(category)
                    && product.getAmount() >= min
                    && product.getAmount() <= max) {
                filteredList.add(product);
            }
        }
        return filteredList;
    }

    private void loadCategories() {
        ApiService apiService = RetrofitClient.getApiService(this);
        String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(getApplicationContext()));
        Call<CategoryResponse> call = apiService.getCategories(bearerToken);

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getData();
                    ArrayAdapter<Category> adapter = new ArrayAdapter<>(SearchedItemActivity.this, android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(SearchedItemActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(SearchedItemActivity.this, "Failed to load categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch(String query) {
        if (!query.isEmpty()) {
            Intent intent = new Intent(SearchedItemActivity.this, SearchedItemActivity.class);
            intent.putExtra("search_query", query);
            intent.putParcelableArrayListExtra("product_list", products);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetFilters() {
        searchTerm.setText("");
        categorySpinner.setSelection(0);
        minPrice.setText("");
        maxPrice.setText("");
    }
}
