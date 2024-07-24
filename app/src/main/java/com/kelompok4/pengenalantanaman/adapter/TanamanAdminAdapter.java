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
import java.util.List;

public class TanamanAdminAdapter extends RecyclerView.Adapter<TanamanAdminAdapter.TanamanViewHolder> {

    private Context context;
    private List<Tanaman> tanamanList;
    private OnItemClickListener listener;
    private List<String> keys; // List to hold keys for corresponding Tanaman objects

    public TanamanAdminAdapter(Context context, List<Tanaman> tanamanList, List<String> keys, OnItemClickListener listener) {
        this.context = context;
        this.tanamanList = tanamanList;
        this.keys = keys;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TanamanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tanamanlist, parent, false);
        return new TanamanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TanamanViewHolder holder, int position) {
        Tanaman tanaman = tanamanList.get(position);
        String key = keys.get(position); // Get the key for the corresponding Tanaman object
        holder.bind(tanaman, key);
    }

    @Override
    public int getItemCount() {
        return tanamanList.size();
    }

    public class TanamanViewHolder extends RecyclerView.ViewHolder {

        private TextView namaTextView;
        private ImageView gambarImageView;

        public TanamanViewHolder(@NonNull View itemView) {
            super(itemView);
            namaTextView = itemView.findViewById(R.id.nama);
            gambarImageView = itemView.findViewById(R.id.gambar);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tanamanList.get(position), keys.get(position));
                }
            });
        }

        public void bind(Tanaman tanaman, String key) {
            namaTextView.setText(tanaman.getNama());
            Glide.with(context)
                    .load(tanaman.getGambar())
                    .placeholder(R.drawable.default_image)
                    .into(gambarImageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Tanaman tanaman, String key);
    }
}
