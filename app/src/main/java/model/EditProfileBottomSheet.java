package model;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rental.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditProfileBottomSheet extends BottomSheetDialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();  // Dismiss the bottom sheet
            }
        });

        view.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fullNameEditText = view.findViewById(R.id.fullNameEditText);
                EditText cityEditText = view.findViewById(R.id.cityEditText);
                EditText phoneNumberEditText = view.findViewById(R.id.phoneNumberEditText);
                ImageView imageView = view.findViewById(R.id.productImage);

                String fullName = fullNameEditText.getText().toString();
                String city = cityEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();


                // Here you can handle the save logic, for now we will just show a toast
                Toast.makeText(getActivity(), "Profile updated!", Toast.LENGTH_SHORT).show();

                dismiss();  // Dismiss the bottom sheet
            }
        });
    }
}
