package com.example.rental.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rental.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Adapter.ItemAdapter;
import retrofit.RetrofitClient;
import retrofit.ApiService;
import retrofit.WishlistResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import model.Product;

public class SavedItemsFragment extends Fragment {

    private static final String TAG = "SavedItemsFragment";
    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private ItemAdapter adapter;
    private List<Product> savedItemsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Starting SavedItemsFragment");
        View view = inflater.inflate(R.layout.fragment_saved_items, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.saved_items_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        progressBar = view.findViewById(R.id.saved_items_progress_bar);
        emptyStateTextView = view.findViewById(R.id.saved_items_empty_state);

        adapter = new ItemAdapter(savedItemsList , getContext());
        recyclerView.setAdapter(adapter);

        // Register Broadcast Receiver
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(wishlistUpdateReceiver,
                new IntentFilter("wishlist-updated"));

        fetchSavedItems(); // Initial fetch

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the broadcast receiver
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(wishlistUpdateReceiver);
        Log.d(TAG, "onDestroyView: BroadcastReceiver unregistered");
    }

    private void fetchSavedItems() {
        Log.d(TAG, "fetchSavedItems: Fetching saved items from backend");
        showLoading(true);
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SecondHandAppPrefs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        apiService.getSavedItems(token).enqueue(new Callback<List<WishlistResponse>>() {
            @Override
            public void onResponse(Call<List<WishlistResponse>> call, Response<List<WishlistResponse>> response) {
                showLoading(false);
                Log.d(TAG, "onResponse: Response received. Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponse: Successful response");
                    savedItemsList.clear();
                    for (WishlistResponse wishlistResponse : response.body()) {
                        Log.d(TAG, "onResponse: Adding product: " + wishlistResponse.getProduct().getName());
                        savedItemsList.add(wishlistResponse.getProduct());
                    }
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: Saved items list updated. Total items: " + savedItemsList.size());
                    showEmptyState(savedItemsList.isEmpty());
                } else {
                    Log.e(TAG, "onResponse: Failed to fetch wishlist. Code: " + response.code() + " Message: " + response.message());
                    showEmptyState(true);
                }
            }

            @Override
            public void onFailure(Call<List<WishlistResponse>> call, Throwable t) {
                showLoading(false);
                showEmptyState(true);
                Log.e(TAG, "onFailure: Failed to fetch wishlist: " + t.getMessage(), t);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        Log.d(TAG, "showLoading: " + (isLoading ? "Showing loading indicator" : "Hiding loading indicator"));
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showEmptyState(boolean isEmpty) {
        Log.d(TAG, "showEmptyState: " + (isEmpty ? "Showing empty state" : "Hiding empty state"));
        emptyStateTextView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private final BroadcastReceiver wishlistUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int productId = intent.getIntExtra("product_id", 0);
            boolean added = intent.getBooleanExtra("added", false);

            Log.d(TAG, "Broadcast received. Product ID: " + productId + ", added: " + added);

            if (added) {
                Log.d(TAG, "onReceive: Fetching saved items due to addition");
                fetchSavedItems(); // Refetch items when something is added
            } else {
                Log.d(TAG, "onReceive: Removing product from list");
                for (Product item : savedItemsList) {
                    if (item.getId() == productId) {
                        savedItemsList.remove(item);
                        Log.d(TAG, "onReceive: Product removed. Product ID: " + productId);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                showEmptyState(savedItemsList.isEmpty());
            }
        }
    };
}
