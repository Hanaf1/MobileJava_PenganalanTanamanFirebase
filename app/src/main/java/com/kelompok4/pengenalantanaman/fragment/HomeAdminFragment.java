package com.kelompok4.pengenalantanaman.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.adapter.UserAdapter;
import com.kelompok4.pengenalantanaman.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminFragment extends Fragment {

    private TextView countUser, countAdmin;
    private ListView listViewPengguna;
    private DatabaseReference usersRef;

    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();

    public HomeAdminFragment() {
        // Required empty public constructor
    }

    public static HomeAdminFragment newInstance(String param1, String param2) {
        HomeAdminFragment fragment = new HomeAdminFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        countAdmin = view.findViewById(R.id.countAdmin);
        countUser = view.findViewById(R.id.countUser);
        fetchUserCounts();

        listViewPengguna = view.findViewById(R.id.listViewPengguna); // Initialize ListView
        userAdapter = new UserAdapter(requireContext(), userList);
        listViewPengguna.setAdapter(userAdapter);

        fetchUserData();

        return view;
    }

    private void fetchUserCounts() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int adminCount = 0;
                int userCount = 0;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String role = userSnapshot.child("role").getValue(String.class);
                    if ("admin".equals(role)) {
                        adminCount++;
                    } else if ("user".equals(role)) {
                        userCount++;
                    }
                }
                countAdmin.setText(String.valueOf(adminCount));
                countUser.setText(String.valueOf(userCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void fetchUserData() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    String created = userSnapshot.child("created").getValue(String.class);

                    userList.add(new User(username, username, created));
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

}
