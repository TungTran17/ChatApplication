package com.example.chatapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityViewProfileBinding;
import com.example.chatapplication.utilities.Constants;
import com.example.chatapplication.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ViewProfileActivity extends AppCompatActivity {
    private ActivityViewProfileBinding binding;
    private String documentId = "";
    private Button button2;
    private String encodeImage = "";
    private PreferenceManager preferenceManager;
    private Bitmap bitmap;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager= new PreferenceManager(getApplicationContext());
        binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindingView();
        bindingAction();

        Intent intent = this.getIntent();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("email", intent.getStringExtra("emailCurrentUser"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                binding.txtName.setText(document.getData().get("name").toString());

                                Object profileImageObject = document.getData().get("image");
                                if (profileImageObject != null) {
                                    imageUrl = profileImageObject.toString();
                                    byte[] bytes = Base64.decode(imageUrl, Base64.DEFAULT);
                                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    binding.avaIv.setImageBitmap(bitmap);
                                }

                                documentId = document.getId();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        binding.btnSave.setOnClickListener(v -> {
            if (binding.txtName.getText().equals("")) {
                showToast("Please enter enough information");
                return;
            } else {
                if(encodeImage.equals("")){

                    encodeImage = imageUrl;
                }
                DocumentReference docRef = db.collection("users").document(documentId);
                docRef.update("name", binding.txtName.getText().toString());
                docRef.update("image", encodeImage);

                preferenceManager.putString(Constants.KEY_NAME, binding.txtName.getText().toString());
                preferenceManager.putString(Constants.KEY_IMAGE, encodeImage);
                showToast("Save changes successfully");

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("emailCurrentUser", intent.getStringExtra("emailCurrentUser"));
                startActivity(i);
            }
        });


    }

    private void bindingAction() {
        button2.setOnClickListener(this::onChangeButtonClick);
    }

    private void onChangeButtonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(intent);

    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            encodeImage = encodeImage(bitmap);
                            setImageViewWithEncodedImage(encodeImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void setImageViewWithEncodedImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.avaIv.setImageBitmap(bitmap);
    }

    private void bindingView() {
        button2 = findViewById(R.id.button2);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap preBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        preBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
