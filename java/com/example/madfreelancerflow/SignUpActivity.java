package com.example.madfreelancerflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private Button btnCreate;
    private CheckBox chkTerms;
    private Spinner spinnerRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        chkTerms = findViewById(R.id.chkTerms);
        btnCreate = findViewById(R.id.btnCreate);
        spinnerRole = findViewById(R.id.spinnerRole);

        // Spinner setup
        String[] roles = {"Client", "Freelancer", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        btnCreate.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String selectedRole = spinnerRole.getSelectedItem().toString();

        // Validation
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter valid email");
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Passwords do not match");
            return;
        }

        if (!chkTerms.isChecked()) {
            Toast.makeText(this, "Accept terms first", Toast.LENGTH_SHORT).show();
            return;
        }

        String role = selectedRole.toLowerCase();

        // Create Firebase Auth user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    String uid = authResult.getUser().getUid();
                    saveUserToFirestore(uid, email, role);

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void saveUserToFirestore(String uid, String email, String role) {

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", uid);
        userMap.put("email", email);
        userMap.put("role", role);

        db.collection("users")
                .document(uid)  // Using Firebase UID as document ID
                .set(userMap)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "SignUp Successful...",
                            Toast.LENGTH_LONG).show();

                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to save user: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }
}
