package com.kelompok4.pengenalantanaman.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.adapter.KategoriAdapter;
import com.kelompok4.pengenalantanaman.model.Kategori;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private RecyclerView recyclerViewKategori;
    private KategoriAdapter kategoriAdapter;
    private List<Kategori> kategoriList;
    private DatabaseReference databaseReference;

    private SearchView searchView; // Add SearchView
    private String currentQuery = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        recyclerViewKategori = view.findViewById(R.id.recyclerViewKategori);
        recyclerViewKategori.setLayoutManager(new LinearLayoutManager(getContext()));

        kategoriList = new ArrayList<>();
        kategoriAdapter = new KategoriAdapter(getContext(), kategoriList);
        recyclerViewKategori.setAdapter(kategoriAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("kategori");
        fetchKategoriData();

        searchView = view.findViewById(R.id.cariKategori);

        // Set listener for SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText.toLowerCase(); // Store lowercase query for case-insensitive search
                kategoriAdapter.filter(currentQuery); // Filter kategori list when text changes
                return true;
            }
        });

        return view;
    }

    private void fetchKategoriData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kategoriList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String namaKategori = dataSnapshot.getKey();
                    Kategori kategori = new Kategori(namaKategori);
                    kategoriList.add(kategori);
                }
                kategoriAdapter.notifyDataSetChanged();
                kategoriAdapter.filter(""); // Tampilkan semua item saat pertama kali di-load
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
