package com.kelompok4.pengenalantanaman.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.model.Tanaman;

public class DetailFragment extends Fragment {

    private static final String ARG_TANAMAN = "arg_tanaman";
    private Tanaman tanaman;

    public static DetailFragment newInstance(Tanaman tanaman) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TANAMAN, tanaman);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView gambarImageView = view.findViewById(R.id.gambar);
        TextView namaTextView = view.findViewById(R.id.nama);
        TextView bagianGunaTextView = view.findViewById(R.id.bagianguna);
        TextView kegunaanTextView = view.findViewById(R.id.kegunaan);

        if (getArguments() != null) {

            tanaman = (Tanaman) getArguments().getSerializable(ARG_TANAMAN);
            if (tanaman != null) {
                namaTextView.setText(tanaman.getNama());
                bagianGunaTextView.setText(tanaman.getBagianGuna());
                kegunaanTextView.setText(tanaman.getKegunaan());
                Glide.with(this)
                        .load(tanaman.getGambar())
                        .placeholder(R.drawable.default_image)
                        .into(gambarImageView);
            }
        }

        // Handle back button click
        view.findViewById(R.id.backButtonContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        return view;
    }
}
