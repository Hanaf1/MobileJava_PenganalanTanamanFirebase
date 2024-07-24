package com.kelompok4.pengenalantanaman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.model.Tanaman;

import java.util.ArrayList;
import java.util.List;

public class TanamanAdapter extends RecyclerView.Adapter<TanamanAdapter.TanamanViewHolder> {

    private Context context;
    private List<Tanaman> tanamanList;
    private List<Tanaman> filteredTanamanList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Tanaman tanaman);
    }

    public TanamanAdapter(Context context, List<Tanaman> tanamanList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.tanamanList = tanamanList;
        this.filteredTanamanList = new ArrayList<>(tanamanList);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TanamanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.tanamanlist, parent, false);
        return new TanamanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TanamanViewHolder holder, int position) {
        Tanaman tanaman;
        if (filteredTanamanList != null && !filteredTanamanList.isEmpty()) {
            tanaman = filteredTanamanList.get(position);
        } else {
            tanaman = tanamanList.get(position);
        }

        holder.nama.setText(tanaman.getNama());

        String imageUrl = tanaman.getGambar();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_image)
                    .into(holder.gambar);
        } else {
            holder.gambar.setImageResource(R.drawable.default_image);
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(tanaman);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (filteredTanamanList != null && !filteredTanamanList.isEmpty()) {
            return filteredTanamanList.size();
        } else {
            return 0;
        }
    }

    static class TanamanViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView nama;

        TanamanViewHolder(View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambar);
            nama = itemView.findViewById(R.id.nama);
        }
    }

    public void updateTanamanList(List<Tanaman> tanamanList) {
        this.tanamanList = tanamanList;
        this.filteredTanamanList = new ArrayList<>(tanamanList);
        notifyDataSetChanged();
    }

    public void updateFilteredTanamanList(List<Tanaman> filteredTanamanList) {
        this.filteredTanamanList = filteredTanamanList;
        notifyDataSetChanged();
    }

    public List<Tanaman> getFilteredTanamanList() {
        return filteredTanamanList;
    }
}
