package com.example.rental;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import model.DeleteAccountBottomSheet;
import retrofit.RetrofitClient;

public class BottomSheetSettingsFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_settings_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();  // Dismiss the bottom sheet
            }
        });

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear shared preferences
                RetrofitClient.clearToken(requireContext());
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SecondHandAppPrefs", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clears all data including the token
                editor.apply();

                // Navigate to LoginActivity and clear the back stack
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();  // Finish the current activity to prevent the user from returning to it
            }
        });

        view.findViewById(R.id.change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
                dismiss();  // Dismiss the bottom sheet
            }
        });

        view.findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle privacy policy
            }
        });

        view.findViewById(R.id.delete_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAccountBottomSheet deleteAccountBottomSheet = new DeleteAccountBottomSheet();
                deleteAccountBottomSheet.show(getParentFragmentManager(), deleteAccountBottomSheet.getTag());
            }
        });

        view.findViewById(R.id.contact_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle contact support
            }
        });

        view.findViewById(R.id.app_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle app name
            }
        });
    }
}
