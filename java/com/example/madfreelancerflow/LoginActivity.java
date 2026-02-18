package com.example.madfreelancerflow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtForgot, txtSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtForgot = findViewById(R.id.txtForgot);
        txtSignup = findViewById(R.id.txtSignup);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        btnLogin.setOnClickListener(v -> loginUser());

        txtForgot.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class))
        );

        txtSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class))
        );
    }

    private void loginUser() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    // 🔥 Fetch role using EMAIL (because document ID is numeric)
                    db.collection("users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {

                                if (queryDocumentSnapshots.isEmpty()) {
                                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    return;
                                }

                                String role = queryDocumentSnapshots
                                        .getDocuments()
                                        .get(0)
                                        .getString("role");

                                if (role == null) role = "client";

                                navigateToDashboard(role.toLowerCase());
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Failed to fetch role", Toast.LENGTH_SHORT).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void navigateToDashboard(String role) {

        Intent intent;

        switch (role) {
            case "client":
                intent = new Intent(this, /*ClientDashboardActivity.class*/ SplashActivity.class);
                break;

            case "freelancer":
                intent = new Intent(this, FreelancerDashboardActivity.class);
                break;

            case "admin":
                intent = new Intent(this, AdminDashboardActivity.class);
                break;

            default:
                Toast.makeText(this, "Invalid role", Toast.LENGTH_SHORT).show();
                auth.signOut();
                return;
        }

        startActivity(intent);
        finish();
    }
}
