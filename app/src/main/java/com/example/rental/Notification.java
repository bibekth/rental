package com.example.rental;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import Adapter.NotificationAdapter;

public class Notification extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<String> notificationList;

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomNavigation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for demonstration
        notificationList = new ArrayList<>();
        notificationList.add("50% off in Helmet recently added. Hurry up now!!!!");
        notificationList.add("Package from your order #123456 has been arrived");
        notificationList.add("Don't miss our new offers!");

        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(notificationAdapter);

        // Set up swipe to remove feature
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // We are not moving items, so return false
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                notificationAdapter.removeItem(position); // Remove the item from the adapter
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.navigation_list) {
                startActivity(new Intent(this, ListActivity.class));
                return true;
            } else if (id == R.id.navigation_notifications) {
                // Already in Notification activity, no need to navigate
                return true;
            } else if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        // Set the selected item in bottom navigation as notifications
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
    }
}
