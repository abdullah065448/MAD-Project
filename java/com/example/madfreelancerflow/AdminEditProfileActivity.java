package com.example.madfreelancerflow;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminEditProfileActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPhone, edtActive;
    private Button btnSave, btnLogout;
    private AdminProfileHelper helper;
    private String adminId;
    private AdminModel currentAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);

        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtActive = findViewById(R.id.edt_active);
        btnSave = findViewById(R.id.btn_save);
        btnLogout = findViewById(R.id.btn_logout);

        helper = new AdminProfileHelper();
        adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadProfile();

        btnSave.setOnClickListener(v -> saveProfile());

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdminEditProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadProfile() {
        helper.getAdminProfile(adminId, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    currentAdmin = task.getResult().toObject(AdminModel.class);
                    if (currentAdmin != null) {
                        edtName.setText(currentAdmin.getName());
                        edtEmail.setText(currentAdmin.getEmail());
                        edtPhone.setText(currentAdmin.getPhoneNumber());
                        edtActive.setText(String.valueOf(currentAdmin.isActive()));
                    }
                } else {
                    Toast.makeText(AdminEditProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveProfile() {
        if (currentAdmin == null) {
            Toast.makeText(this, "Profile not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String activeStr = edtActive.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Name, Email, Phone are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isActive = activeStr.equalsIgnoreCase("true");

        // Prepare map to update only editable fields
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", name);
        updateMap.put("email", email);
        updateMap.put("phoneNumber", phone);
        updateMap.put("active", isActive);

        // Use helper method to update dynamically
        helper.updateAdminProfileFields(adminId, updateMap, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminEditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminEditProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
