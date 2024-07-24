package com.kelompok4.pengenalantanaman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kelompok4.pengenalantanaman.R;
import com.kelompok4.pengenalantanaman.model.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(@NonNull Context context, @NonNull List<User> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_user, parent, false);
        }

        TextView usernameTextView = convertView.findViewById(R.id.textViewUsername);
        TextView createdTextView = convertView.findViewById(R.id.textViewCreated);

        usernameTextView.setText(user.getUsername());
        createdTextView.setText(user.getCreated());

        return convertView;
    }
}
