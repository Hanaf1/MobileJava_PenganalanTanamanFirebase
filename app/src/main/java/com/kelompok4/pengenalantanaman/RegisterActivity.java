package com.kelompok4.pengenalantanaman;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kelompok4.pengenalantanaman.fragment.FirstFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword,  editTextUsername;
    Button buttonRegister;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        editTextUsername = findViewById(R.id.usernameregister);
        editTextEmail = findViewById(R.id.emailregister);
        editTextPassword = findViewById(R.id.passwordregister);
        buttonRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String username, email, password;
                username = String.valueOf(editTextUsername.getText().toString());
                email = String.valueOf(editTextEmail.getText().toString());
                password = String.valueOf(editTextPassword.getText().toString());

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userId = user.getUid();
                                    String username = editTextUsername.getText().toString();

                                    // Format the creation timestamp
                                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                                    String created = sdf.format(new Date());

                                    addUserToDatabase(userId, username, created, "user"); // Default role as "user"

                                    Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void addUserToDatabase(String userId, String username, String created, String role) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("created", created);
        user.put("role", role);

        usersRef.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // Document added successfully
                    Toast.makeText(RegisterActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error adding document
                    Toast.makeText(RegisterActivity.this, "failed register", Toast.LENGTH_SHORT).show();
                });
    }
}
