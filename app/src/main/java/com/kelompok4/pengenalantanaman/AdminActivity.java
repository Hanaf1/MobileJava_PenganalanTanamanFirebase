package com.kelompok4.pengenalantanaman;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok4.pengenalantanaman.fragment.FirstFragment;
import com.kelompok4.pengenalantanaman.fragment.HomeAdminFragment;
import com.kelompok4.pengenalantanaman.fragment.PostAdminFragment;
import com.kelompok4.pengenalantanaman.fragment.SecondFragment;
import com.kelompok4.pengenalantanaman.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    HomeAdminFragment homeadminfragment = new HomeAdminFragment();
    PostAdminFragment postadminfragment = new PostAdminFragment();
    ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        bottomNavigationView = findViewById(R.id.bottomNavAdmin);
        bottomNavigationView.setOnItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fmFragment, homeadminfragment).commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fmFragment, homeadminfragment).commit();
            return true;
        } else if (itemId == R.id.postingan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fmFragment, postadminfragment).commit();
            return true;
        } else if (itemId == R.id.settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fmFragment, thirdFragment).commit();
            return true;
        }
        return false;
    }
}
