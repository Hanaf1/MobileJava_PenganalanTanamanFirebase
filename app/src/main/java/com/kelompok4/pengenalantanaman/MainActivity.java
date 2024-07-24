package com.kelompok4.pengenalantanaman;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelompok4.pengenalantanaman.fragment.FirstFragment;
import com.kelompok4.pengenalantanaman.fragment.SecondFragment;
import com.kelompok4.pengenalantanaman.fragment.ThirdFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Inisialisasi fragment pertama
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();
            return true;
        } else if (itemId == R.id.category) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, secondFragment).commit();
            return true;
        } else if (itemId == R.id.settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, thirdFragment).commit();
            return true;
        }
        return false;
    }

    public void navigateToSecondFragment() {
        // No need for bundle or category argument here
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, secondFragment)
                .addToBackStack(null)
                .commit();

        // Switch to the "Category" tab immediately
        bottomNavigationView.setSelectedItemId(R.id.category);
    }

}
