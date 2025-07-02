package Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rental.R;
import model.Ad;
import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

    private Context context;
    private List<Ad> adList;

    public AdAdapter(Context context, List<Ad> adList) {
        this.context = context;
        this.adList = adList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the correct layout file (item_ad.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.item_ad, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        Ad ad = adList.get(position);
        holder.adName.setText(ad.getName());
        holder.adPrice.setText("NRS " + ad.getPrice());
        holder.adCategory.setText(ad.getCategory());
        holder.adPurchaseYear.setText(ad.getPurchaseYear());

        // Assuming the image is stored as a URI string in the Ad model
        holder.adImage.setImageURI(Uri.parse(ad.getImage()));
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {
        TextView adName, adPrice, adCategory, adPurchaseYear;
        ImageView adImage;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            // Correctly binding the views based on item_ad.xml
            adName = itemView.findViewById(R.id.ad_name);
            adPrice = itemView.findViewById(R.id.ad_price);
            adCategory = itemView.findViewById(R.id.ad_category);
            adPurchaseYear = itemView.findViewById(R.id.ad_purchase_year);
            adImage = itemView.findViewById(R.id.ad_image);
        }
    }
}
