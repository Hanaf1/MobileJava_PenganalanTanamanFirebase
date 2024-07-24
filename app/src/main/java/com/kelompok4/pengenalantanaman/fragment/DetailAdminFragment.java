package com.kelompok4.pengenalantanaman.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.model.Tanaman;
import java.util.HashMap;
import java.util.Map;

public class DetailAdminFragment extends Fragment {

    private static final String ARG_TANAMAN = "arg_tanaman";
    private static final String ARG_KEY = "arg_key";
    private static final String ARG_KATEGORI = "arg_kategori";

    private Tanaman tanaman;
    private String key;
    private String kategori;
    private DatabaseReference tanamanRef;

    private ImageView gambarImageView;
    private TextView namaTextView;
    private TextView bagianGunaTextView;
    private TextView kegunaanTextView;

    public static DetailAdminFragment newInstance(Tanaman tanaman, String key, String kategori) {
        DetailAdminFragment fragment = new DetailAdminFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TANAMAN, tanaman);
        args.putString(ARG_KEY, key);
        args.putString(ARG_KATEGORI, kategori);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_admin, container, false);

        tanamanRef = FirebaseDatabase.getInstance().getReference().child("kategori");

        if (getArguments() != null) {
            tanaman = (Tanaman) getArguments().getSerializable(ARG_TANAMAN);
            key = getArguments().getString(ARG_KEY);
            kategori = getArguments().getString(ARG_KATEGORI);

            gambarImageView = view.findViewById(R.id.gambar);
            namaTextView = view.findViewById(R.id.nama);
            bagianGunaTextView = view.findViewById(R.id.bagianguna);
            kegunaanTextView = view.findViewById(R.id.kegunaan);
            Button updateButton = view.findViewById(R.id.updateButton);
            Button deleteButton = view.findViewById(R.id.deleteButton);

            namaTextView.setText(tanaman.getNama());
            bagianGunaTextView.setText(tanaman.getBagianGuna());
            kegunaanTextView.setText(tanaman.getKegunaan());
            Glide.with(requireContext())
                    .load(tanaman.getGambar())
                    .placeholder(R.drawable.default_image)
                    .into(gambarImageView);

            updateButton.setOnClickListener(v -> updateTanaman());
            deleteButton.setOnClickListener(v -> deleteTanaman());
        }

        return view;
    }

    private void updateTanaman() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Update Tanaman");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_tanaman, null);
        builder.setView(dialogView);

        EditText namaEditText = dialogView.findViewById(R.id.editTextUpdateNamaTanaman);
        EditText bagianGunaEditText = dialogView.findViewById(R.id.editTextUpdateBagianGuna);
        EditText kegunaanEditText = dialogView.findViewById(R.id.editTextUpdateKegunaan);
        EditText gambarEditText = dialogView.findViewById(R.id.editTextUpdateGambarUrl);

        namaEditText.setText(tanaman.getNama());
        bagianGunaEditText.setText(tanaman.getBagianGuna());
        kegunaanEditText.setText(tanaman.getKegunaan());
        gambarEditText.setText(tanaman.getGambar());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = namaEditText.getText().toString().trim();
            String newBagianGuna = bagianGunaEditText.getText().toString().trim();
            String newKegunaan = kegunaanEditText.getText().toString().trim();
            String newGambar = gambarEditText.getText().toString().trim();

            if (!newName.isEmpty() && !newBagianGuna.isEmpty() && !newKegunaan.isEmpty()) {
                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("bagianguna", newBagianGuna);
                updatedData.put("gambar", newGambar);
                updatedData.put("kegunaan", newKegunaan);
                updatedData.put("nama", newName);

                tanamanRef.child(kategori).child("tanaman").child(key).updateChildren(updatedData)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(requireContext(), "Tanaman updated successfully", Toast.LENGTH_SHORT).show();
                            tanaman.setNama(newName);
                            tanaman.setBagianGuna(newBagianGuna);
                            tanaman.setKegunaan(newKegunaan);
                            tanaman.setGambar(newGambar);
                            namaTextView.setText(newName);
                            bagianGunaTextView.setText(newBagianGuna);
                            kegunaanTextView.setText(newKegunaan);
                            Glide.with(requireContext())
                                    .load(newGambar)
                                    .placeholder(R.drawable.default_image)
                                    .into(gambarImageView);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Failed to update tanaman: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteTanaman() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Tanaman");
        builder.setMessage("Are you sure you want to delete this tanaman " + tanaman.getNama() + " ?");
        builder.setPositiveButton("Delete", (dialog, which) -> hapusTanamanDariFirebase());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void hapusTanamanDariFirebase() {
        tanamanRef.child(kategori).child("tanaman").child(key).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Tanaman " + tanaman.getNama() + " deleted successfully ", Toast.LENGTH_SHORT).show();
                    navigateToEditTanamanFragment();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to delete tanaman: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToEditTanamanFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fmFragment, new PostAdminFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
