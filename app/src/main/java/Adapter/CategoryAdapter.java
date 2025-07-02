package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rental.R;
import com.example.rental.SearchedItemActivity;
import com.example.rental.databinding.ItemCategoriesBinding;

import java.util.ArrayList;

import model.Category;
import model.Product;
import retrofit.RetrofitClient;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoriesViewHolder> {

    private final Context context;
    private final ArrayList<Category> categories;
    private final ArrayList<Product> products;  // New: List of all products

    public CategoryAdapter(Context context, ArrayList<Category> categories, ArrayList<Product> products) {
        this.context = context;
        this.categories = categories != null ? categories : new ArrayList<>();
        this.products = products != null ? products : new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Category category = categories.get(position);

        // Set the category name
        holder.binding.cattxt.setText(category.getName() != null ? category.getName() : "Unknown Category");

        // Construct the full image URL using RetrofitClient
        String imageUrl = RetrofitClient.getProductImageUrl(category.getPhoto());
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Load the image using Glide
            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)  // Placeholder while loading
                            .error(R.drawable.placeholder)       // Error image if load fails
                            .centerCrop())                       // Optional: scale type
                    .into(holder.binding.catimg);
        } else {
            holder.binding.catimg.setImageResource(R.drawable.placeholder); // Placeholder image if no photo URL
        }

        // Handle the background color if provided
        try {
            if (category.getColor() != null && !category.getColor().isEmpty()) {
                holder.binding.squareBox.setBackgroundColor(Color.parseColor(category.getColor()));
            } else {
                holder.binding.squareBox.setBackgroundColor(Color.GRAY); // Default color if color is null
            }
        } catch (IllegalArgumentException e) {
            holder.binding.squareBox.setBackgroundColor(Color.GRAY); // Handle color parsing error
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchedItemActivity.class);
            intent.putExtra("selected_category_id", category.getId());  // Pass the category ID
            intent.putParcelableArrayListExtra("product_list", products);  // Pass the entire product list
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        final ItemCategoriesBinding binding;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);
        }
    }
}
