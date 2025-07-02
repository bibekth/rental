package com.example.rental;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.rental.Fragments.ProductListFragment;
import com.example.rental.Fragments.ProfileProductListFragment;
import com.example.rental.Fragments.SavedItemsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import model.Category;
import model.EditProfileBottomSheet;
import model.Product;

public class ProfileActivity extends AppCompatActivity {

    private ProfileProductListFragment productListFragment;
    private View productListUnderline;
    private View savedItemsUnderline;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profilePhoneNumber;

    @Override
    protected void onResume() {
        super.onResume();
        setupNavigation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        loadUserData();
        setupTabs();

        if (savedInstanceState == null) {
            productListFragment = new ProfileProductListFragment();
            openFragment(productListFragment);
            highlightTab(productListUnderline, savedItemsUnderline);
        }

        handleIntentData();
    }

    private void initViews() {
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profilePhoneNumber = findViewById(R.id.profile_phone);

        productListUnderline = findViewById(R.id.product_list_underline);
        savedItemsUnderline = findViewById(R.id.saved_items_underline);

        findViewById(R.id.settings).setOnClickListener(v -> openSettingsCard());

        findViewById(R.id.edit_profile_button).setOnClickListener(v -> openEditProfileCard());
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SecondHandAppPrefs", Context.MODE_PRIVATE);
        profileName.setText(sharedPreferences.getString("name", "Name not available"));
        profileEmail.setText(sharedPreferences.getString("email", "Email not available"));
        profilePhoneNumber.setText(sharedPreferences.getString("phonenumber", "Phone number not available"));
    }

    private void setupNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.navigation_list) {
                startActivity(new Intent(ProfileActivity.this, ListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_notifications) {
                startActivity(new Intent(ProfileActivity.this, Notification.class));
                return true;
            } else if (itemId == R.id.navigation_profile) {
                // Already in ProfileActivity, no need to navigate
                return true;
            } else {
                return false;
            }
        });
    }

    private void setupTabs() {
        findViewById(R.id.product_list_title).setOnClickListener(v -> {
            openFragment(new ProfileProductListFragment());
            highlightTab(productListUnderline, savedItemsUnderline);
        });

        findViewById(R.id.saved_items_title).setOnClickListener(v -> {
            openFragment(new SavedItemsFragment());
            highlightTab(savedItemsUnderline, productListUnderline);
        });
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("name")) {
            String name = intent.getStringExtra("name");
            String description = intent.getStringExtra("description");
            double price = intent.getDoubleExtra("price", 0);
            int id = intent.getIntExtra("id", 0);
            String image = intent.getStringExtra("image");

            Product newProduct = new Product(
                    id,
                    1,
                    name,
                    description,
                    price,
                    image,
                    id,
                    "2024-01-01",
                    new Category(id, "Default Category", "default_image", "#FFFFFF", "2024-01-01", "2024-01-01")
            );

            if (productListFragment == null) {
                productListFragment = new ProfileProductListFragment();
            }
            if (productListFragment.isVisible()) {
                productListFragment.addProduct(newProduct);
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("new_product", newProduct);
                productListFragment.setArguments(bundle);
            }
        }
    }

    private void openSettingsCard() {
        BottomSheetSettingsFragment bottomSheet = new BottomSheetSettingsFragment();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    private void openEditProfileCard() {
        EditProfileBottomSheet bottomSheet = new EditProfileBottomSheet();
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void highlightTab(View toHighlight, View toUnhighlight) {
        toHighlight.setVisibility(View.VISIBLE);
        toUnhighlight.setVisibility(View.GONE);
    }
}
