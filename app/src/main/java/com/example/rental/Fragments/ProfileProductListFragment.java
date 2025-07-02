package com.example.rental.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rental.ProfileActivity;
import com.example.rental.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.ItemAdapter;
import model.Category;
import model.Product;
import retrofit.ApiService;
import retrofit.ProductRequest;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileProductListFragment extends Fragment {

    private static final String TAG = "ProductListFragment";
    private List<Product> productList;
    private ItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.product_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Set span count to 2

        productList = new ArrayList<>();
        adapter = new ItemAdapter(productList, getContext());
        recyclerView.setAdapter(adapter);

        loadProductsFromSharedPreferences(); // Load products from SharedPreferences
        fetchProductsFromBackend(); // Fetch products from backend

        return view;
    }

    private void loadProductsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProductPrefs", getContext().MODE_PRIVATE);

        for (int id = 0; ; id++) {
            String name = sharedPreferences.getString("name_" + id, null);
            if (name == null) break;

            String description = sharedPreferences.getString("description_" + id, null);
            String image = sharedPreferences.getString("image_" + id, null);
            float price = sharedPreferences.getFloat("price_" + id, 0);

            int categoryId = sharedPreferences.getInt("category_id_" + id, 0);
            String purchaseDate = sharedPreferences.getString("purchase_date_" + id, null);

            String categoryName = sharedPreferences.getString("category_name_" + id, "Default Category Name");
            String categoryPhoto = sharedPreferences.getString("category_photo_" + id, "Default Photo URL");
            String categoryColor = sharedPreferences.getString("category_color_" + id, "Default Color");
            String categoryCreatedAt = sharedPreferences.getString("category_created_at_" + id, "Default Created At");
            String categoryUpdatedAt = sharedPreferences.getString("category_updated_at_" + id, "Default Updated At");

            Category category = new Category(categoryId, categoryName, categoryPhoto, categoryColor, categoryCreatedAt, categoryUpdatedAt);

            Product product = new Product(0, 0, name, description, (double) price, image, categoryId, purchaseDate, category);
            productList.add(product);
        }

        adapter.notifyDataSetChanged();
    }

    private void fetchProductsFromBackend() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SecondHandAppPrefs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        ApiService apiService = RetrofitClient.getApiService(getContext());
        Call<ProductRequest> call = apiService.getMyProducts(token);
        call.enqueue(new Callback<ProductRequest>() {
            @Override
            public void onResponse(Call<ProductRequest> call, Response<ProductRequest> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().getData().size() == 0){
                        Toast.makeText(getContext(), "You have no products.", Toast.LENGTH_SHORT).show();
                    }
                    productList.clear();
                    productList.addAll(response.body().getData()); // Extract the list of products
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "onResponse: Failed to fetch products. Response code: " + response.code());
                    Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductRequest> call, Throwable t) {
                Log.e(TAG, "onFailure: Error fetching products", t);
                Toast.makeText(getContext(), "Failed to load products: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addProduct(Product product) {
        productList.add(product);
        adapter.notifyDataSetChanged();
    }
}
