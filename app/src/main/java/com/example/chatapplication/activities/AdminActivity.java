package com.example.chatapplication.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapplication.databinding.ActivityAdminBinding;
import com.example.chatapplication.models.User;
import com.example.chatapplication.utilities.Constants;
import com.example.chatapplication.utilities.PreferenceManager;
import com.example.chatapplication.utilities.UsersAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private PreferenceManager preferenceManager;
    private List<User> userList;
    //private UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        init();
        loadUsers();
    }

    private void init() {
        userList = new ArrayList<>();
        //usersAdapter = new UsersAdapter(userList, this::deleteUserConfirmation);
        binding.adminRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //binding.adminRecyclerView.setAdapter(usersAdapter);
    }

    private void loadUsers() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        userList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            if (!"admin".equals(user.role)) {
                                userList.add(user);
                            }
                        }
                        //usersAdapter.notifyDataSetChanged();
                    } else {
                        showToast("Failed to load users");
                    }
                });
    }

    private void deleteUserConfirmation(int position) {
        User user = userList.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> deleteUser(position))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteUser(int position) {
        User user = userList.get(position);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(user.id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    showToast("User deleted");
                    userList.remove(position);
                    //usersAdapter.notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> showToast("Failed to delete user: " + e.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
