package com.example.madfreelancerflow;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class FreelancerProfileActivity extends AppCompatActivity {

    private EditText etExperience, etHourlyRate, etSkills;
    private Button btnSave;

    private FreelancerProfileHelper profileHelper;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_profile);

        auth = FirebaseAuth.getInstance();
        profileHelper = new FreelancerProfileHelper();

        etExperience = findViewById(R.id.et_experience);
        etHourlyRate = findViewById(R.id.et_hourly_rate);
        etSkills = findViewById(R.id.et_skills);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {

        String freelancerId = auth.getCurrentUser().getUid();

        int experience = Integer.parseInt(etExperience.getText().toString());
        double hourlyRate = Double.parseDouble(etHourlyRate.getText().toString());
        String skills = etSkills.getText().toString();

        profileHelper.updateFreelancerProfile(
                freelancerId,
                "",  // bio
                skills,
                hourlyRate,
                experience,
                "",  // portfolio
                "",  // country
                task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}

