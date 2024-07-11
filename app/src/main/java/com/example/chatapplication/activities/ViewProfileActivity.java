package com.example.chatapplication.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.chatapplication.databinding.ActivityViewProfileBinding;

import com.example.chatapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

public class ViewProfileActivity extends AppCompatActivity {

    private ActivityViewProfileBinding binding;
    private String documentId ="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("email", intent.getStringExtra("email"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("ViewProfileActivity", document.getId() + " => " + document.getData());
                                    binding.txtName.setText(document.getData().get("name").toString());
//                                    binding.txtDOB.setText(document.getData().get("dob").toString());
//                                    binding.radioButton.setChecked(document.getData().get("gender").equals("Male"));
//                                    binding.radioButton2.setChecked(document.getData().get("gender").equals("Female"));
//                                    binding.txtAddress.setText(document.getData().get("address").toString());
//                                    binding.txtPhone.setText(document.getData().get("phone").toString());
                                    documentId = document.getId();
                                }
                            } else {
                                Log.d("ViewProfileActivity", "No user found with the given email");
                                showToast("No user found with the given email");
                            }
                        } else {
                            Log.d("ViewProfileActivity", "Error getting documents: ", task.getException());
                        }
                    }
                });


        binding.btnSave.setOnClickListener(v -> {
            if(binding.txtName.getText().equals("")
            || binding.txtDOB.getText().equals("")
            ||binding.txtPhone.getText().equals("")
            ||binding.txtAddress.getText().equals(""))
            {
                showToast("Please enter enough information");
                return;
            }
            else
            {
                DocumentReference docRef = db.collection("users").document(documentId);
                docRef.update("name", binding.txtName.getText().toString());
                docRef.update("dob",binding.txtDOB.getText().toString());
                docRef.update("gender",binding.radioButton.isChecked() ? "Male" : "Female");
                docRef.update("address",binding.txtAddress.getText().toString());
                docRef.update("phone",binding.txtPhone.getText().toString());
                showToast("Save changes successfully");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}