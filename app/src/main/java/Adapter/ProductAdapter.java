package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rental.ProductDetailActivity;
import com.example.rental.R;
import com.example.rental.databinding.ItemProductBinding;
import model.Product;
import retrofit.RetrofitClient;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<Product> productList;

    // Constructor
    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using View Binding to inflate the layout and bind views
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemProductBinding binding = ItemProductBinding.inflate(inflater, parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Bind product data to the view holder
        Product product = productList.get(position);
        holder.bind(product);

        // Handle item click to navigate to ProductDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Log.e("product_adapter", product.getPurchaseDate() + product.getPhoto());
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());  // Pass the product_id
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getAmount());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_photo", product.getPhoto());
            intent.putExtra("purchase_date", product.getPurchase_date());  // Pass the purchase date to ProductDetailActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to update the list of products
    public void updateList(ArrayList<Product> newProductList) {
        productList.clear();
        productList.addAll(newProductList);
        notifyDataSetChanged();  // Notify the adapter that the data set has changed
    }

    // ViewHolder class using View Binding
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ProductViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Bind data to views using binding
        public void bind(Product product) {
            binding.productName.setText(product.getName());
            binding.productPrice.setText(String.valueOf(product.getAmount()));
            binding.description.setText(product.getDescription());

            // Construct the full image URL using RetrofitClient
            String imageUrl = RetrofitClient.getProductImageUrl(product.getPhoto().trim());
            Log.d("ProductAdapter", "Image URL: " + imageUrl);

            // Load the image using Glide with error logging
            Glide.with(binding.imageView.getContext())
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder) // Placeholder while loading
                            .error(R.drawable.placeholder)      // Error image if load fails
                            .centerCrop())                     // Optional: scale type
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Log the error
                            Log.e("Glide", "Image load failed: " + imageUrl, e);
                            return false; // Return false to let Glide handle the error
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(binding.imageView);
        }
    }
}
