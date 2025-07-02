package com.example.rental;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import model.Category;
import model.Product;

public class ProductManager {

    private static final String PREFS_NAME = "ProductPrefs";
    private SharedPreferences sharedPreferences;

    public ProductManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public ArrayList<Product> loadProducts() {
        ArrayList<Product> products = new ArrayList<>();
        for (int id = 0; ; id++) {
            String name = sharedPreferences.getString("name_" + id, null);
            if (name == null) break; // Stop loading when no more products are found

            String description = sharedPreferences.getString("description_" + id, null);
            String image = sharedPreferences.getString("image_" + id, null);
            float price = sharedPreferences.getFloat("price_" + id, 0);
            int categoryId = sharedPreferences.getInt("category_id_" + id, 0);
            String purchaseDate = sharedPreferences.getString("purchase_date_" + id, null);

            // Retrieve category details from SharedPreferences
            String categoryName = sharedPreferences.getString("category_name_" + id, null);
            String categoryPhoto = sharedPreferences.getString("category_photo_" + id, null);
            String categoryColor = sharedPreferences.getString("category_color_" + id, null);
            String categoryCreatedAt = sharedPreferences.getString("category_createdAt_" + id, null);
            String categoryUpdatedAt = sharedPreferences.getString("category_updatedAt_" + id, null);

            Category category = new Category(categoryId, categoryName, categoryPhoto, categoryColor, categoryCreatedAt, categoryUpdatedAt);

            // Use the constructor with the correct number of parameters
            products.add(new Product(id, 1, name, description, (double) price, image, categoryId, purchaseDate, category));
        }
        return products;
    }

    public void saveProduct(Product product) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name_" + product.getId(), product.getName());
        editor.putString("description_" + product.getId(), product.getDescription());
        editor.putString("image_" + product.getId(), product.getPhoto());
        editor.putFloat("price_" + product.getId(), (float) product.getAmount());
        editor.putInt("category_id_" + product.getId(), product.getCategoryId());
        editor.putString("purchase_date_" + product.getId(), product.getPurchaseDate());

        // Save the category details
        Category category = product.getCategory();
        editor.putString("category_name_" + product.getId(), category.getName());
        editor.putString("category_photo_" + product.getId(), category.getPhoto());
        editor.putString("category_color_" + product.getId(), category.getColor());
        editor.putString("category_createdAt_" + product.getId(), category.getCreatedAt());
        editor.putString("category_updatedAt_" + product.getId(), category.getUpdatedAt());

        editor.apply();
    }
}
