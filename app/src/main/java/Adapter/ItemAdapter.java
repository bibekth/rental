package Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rental.Fragments.OptionsBottomSheetFragment;
import com.example.rental.ProductDetailActivity;
import com.example.rental.ProfileActivity;
import com.example.rental.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import model.Product;
import retrofit.RetrofitClient;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<Product> productList;
    Context context;

    public ItemAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public ItemAdapter(List<Product> productList) {
        this.productList = productList != null ? productList : Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wishlist_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("NRS. " + product.getAmount());

        holder.itemView.setOnClickListener(v -> {
            Log.e("product_adapter", product.getPurchaseDate() + product.getPhoto());
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());  // Pass the product_id
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_description", product.getDescription());
            intent.putExtra("product_price", product.getAmount());
            intent.putExtra("product_rate", product.getRate());
            intent.putExtra("product_photo", product.getPhoto());
            intent.putExtra("purchase_date", product.getPurchase_date());  // Pass the purchase date to ProductDetailActivity
            context.startActivity(intent);
        });

        // Load the image using Picasso
        String imageUrl = product.getPhoto();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (!imageUrl.startsWith("https://")) {
                imageUrl = RetrofitClient.getProductImageUrl(imageUrl); // Ensure this is your actual base URL
            }
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder) // Optional placeholder while loading
                    .error(R.drawable.placeholder) // Optional error image if loading fails
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.placeholder); // Default image if no URL
        }

        holder.optionsButton.setOnClickListener(v -> {
            // Pass the product ID and the listener to the bottom sheet fragment
            OptionsBottomSheetFragment bottomSheet = new OptionsBottomSheetFragment(
                    new OptionsBottomSheetFragment.OptionsListener() {
                        @Override
                        public void onShareClicked() {
                            // Handle share action
                        }

                        @Override
                        public void onRemoveFromWishlistClicked() {
                            // Handle remove from wishlist action
                        }

                        @Override
                        public void onBackClicked() {
                            // Handle back action
                        }
                    },
                    product.getId() // Pass the product ID here
            );
            bottomSheet.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(), bottomSheet.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        ImageButton optionsButton;

        ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            optionsButton = itemView.findViewById(R.id.options_button);
        }
    }
}
