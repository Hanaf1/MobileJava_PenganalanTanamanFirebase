package com.kelompok4.pengenalantanaman;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelompok4.pengenalantanaman.fragment.FirstFragment;
import com.kelompok4.pengenalantanaman.fragment.SecondFragment;
import com.kelompok4.pengenalantanaman.fragment.ThirdFragment;

public class DetailActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView namaTextView = findViewById(R.id.name_detail);
        TextView deskripsiTextView = findViewById(R.id.deskripsi_detail);
        ImageView imageView = findViewById(R.id.image_detail);

        // Get data from intent
        String nama = getIntent().getStringExtra("nama");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Set data to views
        namaTextView.setText(nama);
        deskripsiTextView.setText(deskripsi);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.default_image);
        }

        // Handle back button click
        findViewById(R.id.backButtonContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
