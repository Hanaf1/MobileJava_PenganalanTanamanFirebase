package com.kelompok4.pengenalantanaman.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.fragment.TanamanListFragment;
import com.kelompok4.pengenalantanaman.model.Kategori;

import java.util.ArrayList;
import java.util.List;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder> {

    private Context context;
    private List<Kategori> kategoriList;
    private List<Kategori> filteredKategoriList;

    public KategoriAdapter(Context context, List<Kategori> kategoriList) {
        this.context = context;
        this.kategoriList = kategoriList;
        this.filteredKategoriList = new ArrayList<>(kategoriList);
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kategori_item, parent, false);
        return new KategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriViewHolder holder, int position) {
        Kategori kategori = filteredKategoriList.get(position);
        holder.textViewNamaKategori.setText(kategori.getKategori());

        holder.itemView.setOnClickListener(v -> {
            try {
                Fragment fragment = new TanamanListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("kategori", kategori.getKategori());
                fragment.setArguments(bundle);

                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, fragment)
                        .addToBackStack(null)
                        .commit(); // atau commitAllowingStateLoss()

                Log.d("KategoriAdapter", "Transisi fragment berhasil");
            } catch (Exception e) {
                Log.e("KategoriAdapter", "Error saat transisi fragment: " + e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredKategoriList.size();
    }

    static class KategoriViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNamaKategori;

        KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNamaKategori = itemView.findViewById(R.id.tvKategoriName);
        }
    }

    public void filter(String query) {
        query = query.toLowerCase();
        filteredKategoriList.clear();
        if (query.isEmpty()) {
            filteredKategoriList.addAll(kategoriList);
        } else {
            for (Kategori kategori : kategoriList) {
                if (kategori.getKategori().toLowerCase().contains(query)) {
                    filteredKategoriList.add(kategori);
                }
            }
        }
        notifyDataSetChanged();
    }
}
