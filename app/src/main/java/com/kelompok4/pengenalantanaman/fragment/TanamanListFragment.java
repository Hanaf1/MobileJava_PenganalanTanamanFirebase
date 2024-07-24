package com.kelompok4.pengenalantanaman.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.kelompok4.pengenalantanaman.adapter.TanamanAdapter;
import com.kelompok4.pengenalantanaman.model.Tanaman;

import java.util.ArrayList;
import java.util.List;

public class TanamanListFragment extends Fragment {
    private RecyclerView recyclerViewTanamanList;
    private TanamanAdapter tanamanAdapter;
    private List<Tanaman> tanamanList;
    private DatabaseReference databaseReference;
    private String kategori;

    private SearchView searchView; // Add SearchView
    private List<Tanaman> filteredTanamanList; // List for filtered results

    private String currentQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tanaman_list, container, false);

        recyclerViewTanamanList = view.findViewById(R.id.recyclerViewTanamanList);
        recyclerViewTanamanList.setLayoutManager(new LinearLayoutManager(getContext()));

        tanamanList = new ArrayList<>();
        tanamanAdapter = new TanamanAdapter(getContext(), tanamanList, tanaman -> {
            // Handle item click, navigate to DetailFragment
            Fragment detailFragment = DetailFragment.newInstance(tanaman);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerViewTanamanList.setAdapter(tanamanAdapter);
        if (getArguments() != null) {
            kategori = getArguments().getString("kategori");
        }
        TextView kategoriTextView = view.findViewById(R.id.kategori);
        if (kategori != null) {
            kategoriTextView.setText(kategori);
            databaseReference = FirebaseDatabase.getInstance().getReference("kategori").child(kategori).child("tanaman");
            fetchTanamanData();
        } else {
            kategoriTextView.setText("Kategori tidak ditemukan");
            Toast.makeText(getContext(), "Kategori tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        view.findViewById(R.id.backButtonContainer).setOnClickListener(v -> requireActivity().onBackPressed());

        searchView = view.findViewById(R.id.cariTanaman); // Make sure you have SearchView in your layout (fragment_tanaman_list.xml)
        filteredTanamanList = new ArrayList<>();

        // Set listener for SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentQuery = newText.toLowerCase(); // Store lowercase query for case-insensitive search
                filterTanamanList(); // Filter tanaman list when text changes
                return true;
            }
        });

        return view;
    }

    private void fetchTanamanData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tanamanList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Tanaman tanaman = dataSnapshot.getValue(Tanaman.class);
                    tanamanList.add(tanaman);
                }
                tanamanAdapter.notifyDataSetChanged();
                filterTanamanList(); // Initially filter the tanaman list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterTanamanList() {
        filteredTanamanList.clear();

        if (currentQuery.isEmpty()) {
            // If currentQuery is empty, add all plant items to the filtered list
            filteredTanamanList.addAll(tanamanList);
        } else {
            // If currentQuery is not empty, filter items based on the query
            for (Tanaman tanaman : tanamanList) {
                if (tanaman.getNama().toLowerCase().contains(currentQuery)) {
                    filteredTanamanList.add(tanaman);
                }
            }
        }

        // Update adapter with filtered data
        tanamanAdapter.updateFilteredTanamanList(filteredTanamanList);
    }
}
