package com.kelompok4.pengenalantanaman.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.kelompok4.pengenalantanaman.MainActivity;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.TanamanViewModel;
import com.kelompok4.pengenalantanaman.adapter.HomeAdapter;
import com.kelompok4.pengenalantanaman.model.Tanaman;
import com.kelompok4.pengenalantanaman.model.tanamanClass;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirstFragment extends Fragment {

    private static final String PREFS_NAME = "TanamanPrefs";
    private static final String KEY_LAST_SHOWN_DATE = "lastShownDate";
    private static final String KEY_RANDOM_TANAMAN = "randomTanaman";

    private List<tanamanClass> tanamanList;
    private RecyclerView recyclerView;

    private TextView timeSayTextView;
    private TanamanViewModel tanamanViewModel;

    private ImageView imageView;
    private TextView overlayTanaman, SayName;
    private DatabaseReference databaseReference;
    private List<Tanaman> firebaseTanamanList = new ArrayList<>();
    private Tanaman randomTanaman; // Tambahkan ini untuk melacak tanaman acak

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        SayName = view.findViewById(R.id.SayName);
        timeSayTextView = view.findViewById(R.id.TimeSay);
        tanamanViewModel = new ViewModelProvider(this).get(TanamanViewModel.class);

        // Initialize ThreeTenABP
        AndroidThreeTen.init(requireContext());

        setTimeSayGreeting();

        if (tanamanViewModel.getTanamanList() == null) {
            new LoadJsonTask().execute();
        } else {
            PutDataIntoRecyclerView(tanamanViewModel.getTanamanList());
        }

        imageView = view.findViewById(R.id.imageView);
        overlayTanaman = view.findViewById(R.id.overlayTanaman);

        databaseReference = FirebaseDatabase.getInstance().getReference("kategori");
        fetchRandomTanamanData();

        Button showMoreButton = view.findViewById(R.id.showMoreButton);
        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the updated method navigateToSecondFragment() from MainActivity
                ((MainActivity) requireActivity()).navigateToSecondFragment();
            }
        });

        // Set onClickListener for imageView and overlayTanaman
        View.OnClickListener detailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (randomTanaman != null) {
                    DetailFragment detailFragment = DetailFragment.newInstance(randomTanaman);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.flFragment, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        };

        imageView.setOnClickListener(detailClickListener);
        overlayTanaman.setOnClickListener(detailClickListener);

        fetchUsernameAndDisplay();

        return view;
    }


    private void fetchUsernameAndDisplay() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child("username").getValue(String.class);
                    if (username != null) {
                        SayName.setText("Hello, " + username);
                    } else {
                        SayName.setText("Hello, User");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to fetch username", Toast.LENGTH_SHORT).show();
                    SayName.setText("Hello, User");
                }
            });
        }
    }

    private void setTimeSayGreeting() {
        LocalTime currentTime = LocalTime.now();
        String greeting;

        if (currentTime.isBefore(LocalTime.NOON)) {
            greeting = "Good Morning";
        } else if (currentTime.isBefore(LocalTime.of(18, 0))) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        timeSayTextView.setText(greeting);
    }

    private void fetchRandomTanamanData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseTanamanList.clear();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    DataSnapshot tanamanSnapshot = categorySnapshot.child("tanaman");
                    for (DataSnapshot dataSnapshot : tanamanSnapshot.getChildren()) {
                        Tanaman tanaman = dataSnapshot.getValue(Tanaman.class);
                        if (tanaman != null) {
                            firebaseTanamanList.add(tanaman);
                        }
                    }
                }
                if (!firebaseTanamanList.isEmpty()) {
                    checkAndDisplayRandomTanaman();
                } else {
                    Log.e("FirstFragment", "firebaseTanamanList is empty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                Log.e("FirstFragment", "DatabaseError: " + error.getMessage());
            }
        });
    }

    private void checkAndDisplayRandomTanaman() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastShownDate = prefs.getString(KEY_LAST_SHOWN_DATE, "");
        String currentDate = LocalDate.now().toString();

        if (!currentDate.equals(lastShownDate)) {
            displayRandomTanaman();

            // Simpan tanggal saat ini dan tanaman acak ke SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_LAST_SHOWN_DATE, currentDate);
            editor.putString(KEY_RANDOM_TANAMAN, new Gson().toJson(randomTanaman));
            editor.apply();
        } else {
            // Tampilkan tanaman yang sama seperti yang disimpan di SharedPreferences
            String randomTanamanJson = prefs.getString(KEY_RANDOM_TANAMAN, null);
            if (randomTanamanJson != null) {
                randomTanaman = new Gson().fromJson(randomTanamanJson, Tanaman.class);
                updateUIWithTanaman(randomTanaman);
            } else {
                displayRandomTanaman();
            }
        }
    }

    private void displayRandomTanaman() {
        Random random = new Random();
        int randomIndex = random.nextInt(firebaseTanamanList.size());
        randomTanaman = firebaseTanamanList.get(randomIndex); // Simpan Tanaman acak

        if (randomTanaman != null) {
            updateUIWithTanaman(randomTanaman);

            Log.d("FirstFragment", "Displaying Tanaman: " + randomTanaman.getNama());
        } else {
            Log.e("FirstFragment", "randomTanaman is null");
        }
    }

    private void updateUIWithTanaman(Tanaman tanaman) {
        overlayTanaman.setText(tanaman.getNama());

        Glide.with(this)
                .load(tanaman.getGambar())
                .placeholder(R.drawable.default_image)
                .into(imageView);
    }

    private class LoadJsonTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                InputStream is = requireActivity().getAssets().open("tanaman_herbal.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                return new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            if (json != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<tanamanClass>>() {}.getType();
                tanamanList = gson.fromJson(json, listType);
                PutDataIntoRecyclerView(tanamanList);
            }
        }
    }

    private void PutDataIntoRecyclerView(List<tanamanClass> tanamanList) {
        List<tanamanClass> limitedList = tanamanList.subList(0, Math.min(tanamanList.size(), 6));

        HomeAdapter homeadapter = new HomeAdapter(getActivity(), limitedList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeadapter);
    }
}
