package com.example.chatapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapplication.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        setListeners();
    }

    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonReset.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                binding.inputEmail.setError("Email field can't be empty");
            } else if (!isValidEmail(email)) {
                binding.inputEmail.setError("Please enter a valid email");
            } else {
                resetPassword(email);
            }
        });
    }

    private void resetPassword(String email) {
        binding.forgetPasswordProgressbar.setVisibility(View.VISIBLE);
        binding.buttonReset.setVisibility(View.INVISIBLE);

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent successfully", Toast.LENGTH_SHORT).show();
                    binding.forgetPasswordProgressbar.setVisibility(View.INVISIBLE);
                    binding.buttonReset.setVisibility(View.VISIBLE);
                    // Chuyển hướng đến SignInActivity sau khi gửi email đặt lại mật khẩu thành công
                    Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send password reset email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.forgetPasswordProgressbar.setVisibility(View.INVISIBLE);
                    binding.buttonReset.setVisibility(View.VISIBLE);
                    Log.e("ForgotPasswordActivity", "Error sending password reset email", e);
                });
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
