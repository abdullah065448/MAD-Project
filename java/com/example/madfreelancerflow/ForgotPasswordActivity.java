package com.example.madfreelancerflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ForgotPasswordActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextInputEditText emailInput;
    Button btnLogin;
    TextView tvResend, tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.edtEmail);
        btnLogin = findViewById(R.id.btnLogin);
        tvResend = findViewById(R.id.tvResend);
        tvBack = findViewById(R.id.tvBackToLogin);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        btnLogin.setOnClickListener(view -> resetPassword());
        tvResend.setOnClickListener(view -> resetPassword());

        tvBack.setOnClickListener(view -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });
    }

    void resetPassword() {

        String email = emailInput.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(ForgotPasswordActivity.this,
                                "Reset link sent to email",
                                Toast.LENGTH_LONG).show();

                        // ✅ Move to Login Activity after success
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(ForgotPasswordActivity.this,
                                "Failed to send reset link",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
