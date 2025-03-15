package com.mankind.e_commerce.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCallerLauncher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mankind.e_commerce.MainActivity;
import com.mankind.e_commerce.R;
import com.mankind.e_commerce.databinding.ActivityNewProductBinding;
import com.mankind.e_commerce.model.ProductModel;
import com.mankind.e_commerce.viewmodel.ViewModel;

public class NewProductActivity extends AppCompatActivity {
    private ActivityNewProductBinding binding;
    private ViewModel viewModel;
    private Uri imageUri;
    private ActivityResultLauncher takeImage;
    private FirebaseAuth mAuth;
    private FirebaseStorage storageReference;
    String[] categoryNames = {"Shoes", "Shirts", "Bags", "Pants"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_product);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance();
        takeImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                imageUri = o;
                binding.productImage.setImageURI(imageUri);
            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryNames);
        binding.productCategory.setAdapter(arrayAdapter);

        binding.fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage.launch("image/*");
            }
        });

        binding.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loadingContainer.setVisibility(View.VISIBLE);
                String productName = binding.productName.getText().toString().trim();
                String productCategory = binding.productCategory.getText().toString().trim();
                String productPrice = binding.productPrice.getText().toString().trim();
                String ratings = binding.ratings.getText().toString().trim();
                String productBio = binding.productBio.getText().toString().trim();
                if (!productName.isEmpty() && !productCategory.isEmpty() && !productPrice.isEmpty() && !ratings.isEmpty() && !productBio.isEmpty()) {
                    if(imageUri != null){
                        StorageReference filePath = storageReference.getReference("Product Images")
                                .child("image" + Timestamp.now().getNanoseconds());
                        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        binding.loadingContainer.setVisibility(View.GONE);
                                        String imageUrl = uri.toString();
                                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection(productCategory).document();
                                        ProductModel productModel = new ProductModel(productName, mAuth.getCurrentUser().getUid(), productBio, "$" + productPrice, ratings, documentReference.getId(), productCategory, imageUrl);
                                        viewModel.addProducts(productModel, productCategory, documentReference.getId(), NewProductActivity.this);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.loadingContainer.setVisibility(View.GONE);
                                        Toast.makeText(NewProductActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                binding.loadingContainer.setVisibility(View.GONE);
                                Toast.makeText(NewProductActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        binding.loadingContainer.setVisibility(View.GONE);
                        Toast.makeText(NewProductActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    binding.loadingContainer.setVisibility(View.GONE);
                    Toast.makeText(NewProductActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });



        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewProductActivity.this, MainActivity.class));
            }
        });
    }
}