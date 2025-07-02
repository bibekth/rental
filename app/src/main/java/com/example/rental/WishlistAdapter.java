package com.example.rental;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.rental.R;

import retrofit.RetrofitClient;
import retrofit.WishlistResponse;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<WishlistResponse> wishlistResponseList;

    public WishlistAdapter(List<WishlistResponse> wishlistResponseList) {
        this.wishlistResponseList = wishlistResponseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WishlistResponse wishlistResponse = wishlistResponseList.get(position);
        // Bind the data to the views here
        holder.productName.setText(wishlistResponse.getProduct().getName());
        holder.productPrice.setText("Rs " + wishlistResponse.getProduct().getAmount());
        String imageUrl = RetrofitClient.getProductImageUrl(wishlistResponse.getProduct().getPhoto() );
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.productImage);

//        holder.itemView.setOnClickListener(v -> {
//            Log.e("product_adapter", product.getPurchaseDate() + product.getPhoto());
//            Intent intent = new Intent(context, ProductDetailActivity.class);
//            intent.putExtra("product_id", product.getId());  // Pass the product_id
//            intent.putExtra("product_name", product.getName());
//            intent.putExtra("product_price", product.getAmount());
//            intent.putExtra("product_description", product.getDescription());
//            intent.putExtra("product_photo", product.getPhoto());
//            intent.putExtra("purchase_date", product.getPurchase_date());  // Pass the purchase date to ProductDetailActivity
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return wishlistResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
