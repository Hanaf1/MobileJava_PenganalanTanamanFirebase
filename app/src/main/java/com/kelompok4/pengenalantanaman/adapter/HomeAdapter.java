package com.kelompok4.pengenalantanaman.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kelompok4.pengenalantanaman.DetailActivity;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.model.tanamanClass;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<tanamanClass> tanamanList;

    public HomeAdapter(Context context, List<tanamanClass> tanamanList) {
        this.context = context;
        this.tanamanList = tanamanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        tanamanClass tanaman = tanamanList.get(position);
        holder.nama.setText(tanaman.getNama());

        String imageUrl = tanaman.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context).load(imageUrl).into(holder.gambar);
        } else {
            holder.gambar.setImageResource(R.drawable.default_image);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("nama", tanaman.getNama());
            intent.putExtra("deskripsi", tanaman.getDeskripsi());
            intent.putExtra("imageUrl", tanaman.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tanamanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        ImageView gambar;

        public ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.name_item);
            gambar = itemView.findViewById(R.id.image_item);
        }
    }
}
