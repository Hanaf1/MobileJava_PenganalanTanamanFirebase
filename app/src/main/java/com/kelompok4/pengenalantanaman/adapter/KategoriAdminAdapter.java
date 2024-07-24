package com.kelompok4.pengenalantanaman.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.fragment.EditTanamanFragment;
import com.kelompok4.pengenalantanaman.fragment.TanamanListFragment;
import com.kelompok4.pengenalantanaman.model.Kategori;

import java.util.List;

public class KategoriAdminAdapter extends RecyclerView.Adapter<KategoriAdminAdapter.KategoriViewHolder> {

    private final Context context;
    private final List<Kategori> kategoriList;
    private final DatabaseReference kategoriRef;
    private final DatabaseReference tanamanRef;

    public KategoriAdminAdapter(Context context, List<Kategori> kategoriList) {
        this.context = context;
        this.kategoriList = kategoriList;
        this.kategoriRef = FirebaseDatabase.getInstance().getReference("kategori");
        this.tanamanRef = FirebaseDatabase.getInstance().getReference("tanaman");
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kategoriadmin_item, parent, false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriViewHolder holder, int position) {
        final Kategori kategori = kategoriList.get(position);
        holder.textViewNamaKategori.setText(kategori.getKategori());

        holder.itemView.setOnClickListener(v -> {
            try {
                Fragment fragment = new EditTanamanFragment();
                Bundle bundle = new Bundle();
                bundle.putString("kategori", kategori.getKategori());
                fragment.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fmFragment, fragment)
                        .addToBackStack(null)
                        .commit();
            } catch (Exception e) {
                Log.e("KategoriAdapter", "Error during fragment transition: " + e.getMessage());
            }
        });

        // Update Button
        holder.ivUpdate.setOnClickListener(v -> showEditDialog(kategori.getKategori()));

        // Delete Button
        holder.ivDelete.setOnClickListener(v -> showDeleteConfirmationDialog(kategori.getKategori()));
    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    // Dialog for editing the category
    private void showEditDialog(String oldKategori) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Category");

        EditText input = new EditText(context);
        input.setText(oldKategori);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newKategori = input.getText().toString().trim();
            if (!newKategori.isEmpty()) {
                editKategoriKeFirebase(oldKategori, newKategori);
            } else {
                Toast.makeText(context, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Dialog to confirm category deletion
    private void showDeleteConfirmationDialog(String kategori) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Category");
        builder.setMessage("Are you sure you want to delete this category?");

        builder.setPositiveButton("Delete", (dialog, which) -> hapusKategoriDariFirebase(kategori));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Method to update category in Firebase (with data preservation)
    private void editKategoriKeFirebase(String oldKategori, String newKategori) {
        DatabaseReference oldRef = kategoriRef.child(oldKategori);
        DatabaseReference newRef = kategoriRef.child(newKategori);

        oldRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newRef.setValue(snapshot.getValue())
                        .addOnSuccessListener(unused -> {
                            oldRef.removeValue();
                            Toast.makeText(context, "Category updated successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Failed to update category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to update category: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to delete category from Firebase
    private void hapusKategoriDariFirebase(String kategori) {
        kategoriRef.child(kategori).removeValue()
                .addOnSuccessListener(unused -> Toast.makeText(context, "Category deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete category: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    static class KategoriViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNamaKategori;
        ImageButton ivUpdate, ivDelete;

        KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNamaKategori = itemView.findViewById(R.id.tvKategoriName);
            ivUpdate = itemView.findViewById(R.id.ivUpdate);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
