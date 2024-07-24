package com.kelompok4.pengenalantanaman.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.adapter.KategoriAdminAdapter;
import com.kelompok4.pengenalantanaman.model.Kategori;

import java.util.ArrayList;
import java.util.List;

public class PostAdminFragment extends Fragment {

    private RecyclerView recyclerViewKategori;
    private KategoriAdminAdapter kategoriAdminAdapter;
    private List<Kategori> kategoriList;
    private DatabaseReference databaseReference;
    private Button tambahKategori;

    public PostAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_admin, container, false);

        recyclerViewKategori = view.findViewById(R.id.recyclerViewKategori);
        recyclerViewKategori.setLayoutManager(new LinearLayoutManager(getContext()));

        kategoriList = new ArrayList<>();
        kategoriAdminAdapter = new KategoriAdminAdapter(getContext(), kategoriList);
        recyclerViewKategori.setAdapter(kategoriAdminAdapter);

        tambahKategori = view.findViewById(R.id.buttonTambahKategori);
        tambahKategori.setOnClickListener(v -> showTambahKategoriDialog());

        databaseReference = FirebaseDatabase.getInstance().getReference("kategori");
        fetchKategoriData();

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
                kategoriAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTambahKategoriDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tambah Kategori");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tambah_kategori, (ViewGroup) getView(), false);
        final EditText input = viewInflated.findViewById(R.id.editTextNamaKategori);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String namaKategori = input.getText().toString();
            tambahKategoriKeFirebase(namaKategori);
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void tambahKategoriKeFirebase(String namaKategori) {
        if (!namaKategori.isEmpty()) {
            // Add the new category with a child "tanaman"
            databaseReference.child(namaKategori).child("tanaman").setValue("").addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Kategori berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Gagal menambahkan kategori", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Nama kategori tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }
}
