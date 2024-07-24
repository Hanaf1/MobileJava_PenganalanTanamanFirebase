package com.kelompok4.pengenalantanaman.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.adapter.TanamanAdminAdapter;
import com.kelompok4.pengenalantanaman.model.Tanaman;

import java.util.ArrayList;
import java.util.List;

public class EditTanamanFragment extends Fragment {

    private RecyclerView recyclerViewTanamanList;
    private TanamanAdminAdapter tanamanAdapter;
    private List<Tanaman> tanamanList;
    private List<String> keys; // List to hold keys for corresponding Tanaman objects
    private DatabaseReference databaseReference;
    private String kategori;

    private Button tambahTanaman;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_tanaman, container, false);

        recyclerViewTanamanList = view.findViewById(R.id.recyclerViewTanamanList);
        recyclerViewTanamanList.setLayoutManager(new LinearLayoutManager(getContext()));

        tambahTanaman = view.findViewById(R.id.buttonTambahTanaman);
        tambahTanaman.setOnClickListener(v -> showTambahTanamanDialog());


        tanamanList = new ArrayList<>();
        keys = new ArrayList<>(); // Initialize keys list
        tanamanAdapter = new TanamanAdminAdapter(getContext(), tanamanList, keys, (tanaman, key) -> {
            // Handle item click, navigate to DetailFragment
            Fragment detailAdminFragment = DetailAdminFragment.newInstance(tanaman, key, kategori); // Mengirim kategori ke DetailAdminFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fmFragment, detailAdminFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerViewTanamanList.setAdapter(tanamanAdapter);

        if (getArguments() != null) {
            kategori = getArguments().getString("kategori");
        }

        TextView kategoriTextView = view.findViewById(R.id.kategori);
        if (kategori != null) {
            kategoriTextView.setText("Edit Postingan : " + kategori);
            databaseReference = FirebaseDatabase.getInstance().getReference("kategori").child(kategori).child("tanaman");
            fetchTanamanData();
        } else {
            kategoriTextView.setText("Kategori tidak ditemukan");
            Toast.makeText(getContext(), "Kategori tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        view.findViewById(R.id.backButtonContainer).setOnClickListener(v -> requireActivity().onBackPressed());


        return view;
    }

    private void fetchTanamanData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tanamanList.clear();
                keys.clear(); // Clear keys list before fetching new data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tanaman tanaman = dataSnapshot.getValue(Tanaman.class);
                    String key = dataSnapshot.getKey();
                    tanamanList.add(tanaman);
                    keys.add(key);
                }
                tanamanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTambahTanamanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tambah Tanaman");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tambah_tanaman, (ViewGroup) getView(), false);
        final EditText inputBagianguna = viewInflated.findViewById(R.id.editTextBagianguna);
        final EditText inputGambar = viewInflated.findViewById(R.id.editTextGambar);
        final EditText inputKegunaan = viewInflated.findViewById(R.id.editTextKegunaan);
        final EditText inputNama = viewInflated.findViewById(R.id.editTextNama);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String bagianguna = inputBagianguna.getText().toString();
            String gambar = inputGambar.getText().toString();
            String kegunaan = inputKegunaan.getText().toString();
            String nama = inputNama.getText().toString();
            tambahTanamanKeFirebase(bagianguna, gambar, kegunaan, nama);
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void tambahTanamanKeFirebase(String bagianguna, String gambar, String kegunaan, String nama) {
        if (!bagianguna.isEmpty() && !gambar.isEmpty() && !kegunaan.isEmpty() && !nama.isEmpty()) {
            // Generate a unique key for the new plant
            String key = databaseReference.push().getKey();
            if (key != null) {
                Tanaman tanaman = new Tanaman();
                databaseReference.child(key).setValue(tanaman).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Tanaman berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Gagal menambahkan tanaman", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Gagal menghasilkan kunci unik", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
        }
    }
}


