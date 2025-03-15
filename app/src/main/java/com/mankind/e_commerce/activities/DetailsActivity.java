package com.mankind.e_commerce.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.mankind.e_commerce.R;
import com.mankind.e_commerce.databinding.ActivityDetailsBinding;
import com.mankind.e_commerce.model.CartProductModel;
import com.mankind.e_commerce.model.ProductModel;
import com.mankind.e_commerce.viewmodel.ViewModel;
import com.mankind.e_commerce.viewmodel.livedatatext.QuantityText;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        getSupportActionBar().hide();

        viewModel.getSelectedProduct(getIntent().getStringExtra("productId"), getIntent().getStringExtra("collectionName"), DetailsActivity.this).observe(this, new Observer<ProductModel>() {
            @Override
            public void onChanged(ProductModel productModel) {
                Glide.with(getApplicationContext())
                        .load(productModel.getProductImageUrl())
                        .centerCrop()
                        .into(binding.productImage);
                binding.productName.setText(productModel.getProductName());
                binding.productBio.setText(productModel.getProductBio());
                binding.productPrice.setText(productModel.getProductPrice());
                binding.productRatings.setText(productModel.getRatings());
            }
        });

        QuantityText quantityText = new QuantityText();

        quantityText.getQuantity().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.productQuantity.setText(""+integer);
            }
        });

        binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = binding.productName.getText().toString().trim();
                String productBio = binding.productBio.getText().toString().trim();
                String productPrice = binding.productPrice.getText().toString().trim();
                String ratings = binding.productRatings.getText().toString().trim();
                String productId = getIntent().getStringExtra("productId");
                String productCategory = getIntent().getStringExtra("collectionName");
                String productImageUrl = getIntent().getStringExtra("imageUrl");
                CartProductModel cartProductModel = new CartProductModel(productName, getIntent().getStringExtra("merchantId"), productBio, productPrice, ratings, productId, productCategory,productImageUrl, binding.productQuantity.getText().toString().trim());
                viewModel.addProductsToCart(cartProductModel, DetailsActivity.this);
            }
        });
        binding.increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityText.increaseQuantity();
            }
        });

        binding.decreaseButton.setOnClickListener( v -> {
            quantityText.decreaseQuantity();
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
}