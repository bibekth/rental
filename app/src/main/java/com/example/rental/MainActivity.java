package com.example.rental;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rental.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import Adapter.CategoryAdapter;
import Adapter.ImageSliderAdapter;
import Adapter.ProductAdapter;
import model.Category;
import model.Product;
import retrofit.ApiService;
import retrofit.CategoryResponse;
import retrofit.ProductRequest;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int ADD_PRODUCT_REQUEST = 1;

    private ActivityMainBinding binding;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categories;

    private ProductAdapter mainProductAdapter;
    private ProductAdapter additionalProductAdapter;
    private ArrayList<Product> products;
    private ArrayList<Product> mainProducts;
    private ArrayList<Product> additionalProducts;

    private ViewPager2 viewPager2;
    private ImageSliderAdapter imageSliderAdapter;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            Log.d(TAG, "onCreate: MainActivity started.");


            // Initialize categories and image slider
            initImageSlider();

            setupSearchBar();
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Exception during initialization", e);
            Toast.makeText(this, "Initialization error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();  // If an error occurs during initialization, close the activity gracefully
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation();
        fetchProductsFromBackend();
        initCategories();
    }

    private void setupSearchBar() {
        try {
            EditText searchBar = findViewById(R.id.search_bar);
            searchBar.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    performSearch(searchBar.getText().toString().trim());
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
            Log.e(TAG, "setupSearchBar: Error setting up search bar", e);
            Toast.makeText(this, "Error setting up search bar", Toast.LENGTH_SHORT).show();
        }
    }

    private void performSearch(String query) {
        if (!query.isEmpty()) {
            try {
                Intent intent = new Intent(MainActivity.this, SearchedItemActivity.class);
                intent.putExtra("search_query", query);
                intent.putParcelableArrayListExtra("product_list", products);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "performSearch: Error starting search activity", e);
                Toast.makeText(this, "Error performing search", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        try {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    return true;
                } else if (id == R.id.navigation_list) {
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    startActivityForResult(intent, ADD_PRODUCT_REQUEST);
                    return true;
                } else if (id == R.id.navigation_notifications) {
                    // Navigation to Notification Activity
                    Intent intent = new Intent(MainActivity.this, Notification.class);
                    startActivity(intent);  // Start the Notification activity
                    return true;
                } else if (id == R.id.navigation_profile) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putParcelableArrayListExtra("product_list", products);
                    startActivity(intent);
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
            Log.e(TAG, "setupBottomNavigation: Error setting up bottom navigation", e);
            Toast.makeText(this, "Error setting up bottom navigation", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchProductsFromBackend() {
        ApiService apiService = RetrofitClient.getApiService(this);
        String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(getApplicationContext()));

        Call<ProductRequest> call = apiService.getProducts(bearerToken);

        call.enqueue(new Callback<ProductRequest>() {
            @Override
            public void onResponse(Call<ProductRequest> call, Response<ProductRequest> response) {
                if (response.isSuccessful() && response.body() != null) {
                    products = new ArrayList<>(response.body().getData());
                    updateProductLists(); // Update UI with fetched products
                } else {
                    Log.e(TAG, "fetchProductsFromBackend: Failed to load products, response code: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductRequest> call, Throwable t) {
                Log.e(TAG, "fetchProductsFromBackend: Failed to load products", t);
                Toast.makeText(MainActivity.this, "Failed to load products: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initCategories() {
        categories = new ArrayList<>();
        fetchCategoriesFromBackend(); // Dynamically fetch categories from the backend
    }

    void fetchCategoriesFromBackend() {
        ApiService apiService = RetrofitClient.getApiService(this);
        String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(getApplicationContext()));
        Call<CategoryResponse> call = apiService.getCategories(bearerToken);

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = new ArrayList<>(response.body().getData());
                    categoryAdapter = new CategoryAdapter(MainActivity.this, categories, products);

                    GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 4);
                    binding.categoriesList.setLayoutManager(layoutManager);
                    binding.categoriesList.setAdapter(categoryAdapter);
                } else {
                    Log.e(TAG, "fetchCategoriesFromBackend: Failed to load categories, response code: " + response.code());
                    Toast.makeText(MainActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.e(TAG, "fetchCategoriesFromBackend: Failed to load categories", t);
                Toast.makeText(MainActivity.this, "Failed to load categories: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    void updateProductLists() {
        mainProducts = new ArrayList<>();
        additionalProducts = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            if (i < 6) {
                mainProducts.add(products.get(i));
            } else {
                additionalProducts.add(products.get(i));
            }
        }

        try {
            mainProductAdapter = new ProductAdapter(this, mainProducts);
            additionalProductAdapter = new ProductAdapter(this, additionalProducts);

            GridLayoutManager mainLayoutManager = new GridLayoutManager(this, 3);
            binding.productList.setLayoutManager(mainLayoutManager);
            binding.productList.setAdapter(mainProductAdapter);

            GridLayoutManager additionalLayoutManager = new GridLayoutManager(this, 3);
            binding.additionalProductList.setLayoutManager(additionalLayoutManager);
            binding.additionalProductList.setAdapter(additionalProductAdapter);

            additionalProductAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "updateProductLists: Error setting up product adapters", e);
            Toast.makeText(this, "Error displaying products", Toast.LENGTH_SHORT).show();
        }
    }

    void initImageSlider() {
        try {
            viewPager2 = findViewById(R.id.viewPager);

            List<Integer> imageList = new ArrayList<>();
            imageList.add(R.drawable.a);
            imageList.add(R.drawable.a);
            imageList.add(R.drawable.a);

            imageSliderAdapter = new ImageSliderAdapter(this, imageList);
            viewPager2.setAdapter(imageSliderAdapter);

            Runnable sliderRunnable = new Runnable() {
                @Override
                public void run() {
                    if (viewPager2.getCurrentItem() == imageList.size() - 1) {
                        viewPager2.setCurrentItem(0);
                    } else {
                        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                    }
                    sliderHandler.postDelayed(this, 2000);
                }
            };

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    sliderHandler.removeCallbacks(sliderRunnable);
                    sliderHandler.postDelayed(sliderRunnable, 2000);
                }
            });

            sliderHandler.postDelayed(sliderRunnable, 2000);
        } catch (Exception e) {
            Log.e(TAG, "initImageSlider: Error initializing image slider", e);
            Toast.makeText(this, "Error initializing image slider", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            sliderHandler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            Log.e(TAG, "onDestroy: Error during destruction", e);
        }
    }
}
