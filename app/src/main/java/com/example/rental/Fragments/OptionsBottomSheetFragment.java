package com.example.rental.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rental.R;
import retrofit.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionsBottomSheetFragment extends BottomSheetDialogFragment {

    private OptionsListener optionsListener;
    private int productId;

    public interface OptionsListener {
        void onShareClicked();
        void onRemoveFromWishlistClicked();
        void onBackClicked();
    }

    public OptionsBottomSheetFragment(OptionsListener optionsListener, int productId) {
        this.optionsListener = optionsListener;
        this.productId = productId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        TextView share = view.findViewById(R.id.share);
        TextView deleteProduct = view.findViewById(R.id.delete_product);
        ImageView cancelButton = view.findViewById(R.id.cancelButton);

        // Handle share click
        share.setOnClickListener(v -> {
            optionsListener.onShareClicked();
            dismiss();
        });

        // Handle delete product click
        deleteProduct.setOnClickListener(v -> {
            deleteProductFromDatabase(getContext(), productId);
        });

        // Handle cancel button click
        cancelButton.setOnClickListener(v -> {
            dismiss(); // Dismiss the bottom sheet
        });

        return view;
    }

    private void deleteProductFromDatabase(Context context, int productId) {
        // Get ApiService from RetrofitClient
        String bearerToken = RetrofitClient.getBearerToken(RetrofitClient.getToken(requireContext()));
        Call<Void> call = RetrofitClient.getApiService(context).deleteProduct(bearerToken, productId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    optionsListener.onRemoveFromWishlistClicked();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
