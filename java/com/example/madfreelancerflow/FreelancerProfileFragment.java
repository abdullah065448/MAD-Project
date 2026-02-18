package com.example.madfreelancerflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class FreelancerProfileFragment extends Fragment {

    private TextView txtName, txtEmail, txtExperience, txtHourlyRate, txtSkills;
    private Button btnEditProfile, btnLogout;

    private FirebaseAuth auth;
    private FreelancerProfileHelper profileHelper;

    // Required empty constructor
    public FreelancerProfileFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_freelancer_profile, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        profileHelper = new FreelancerProfileHelper();

        // Bind Views
        txtName = view.findViewById(R.id.txt_name);
        txtEmail = view.findViewById(R.id.txt_email);
        txtExperience = view.findViewById(R.id.txt_experience);
        txtHourlyRate = view.findViewById(R.id.txt_hourly_rate);
        txtSkills = view.findViewById(R.id.txt_skills);

        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_logout);

        // Logout Button
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        // Edit Profile Button
        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(),
                    FreelancerProfileActivity.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile(); // Refresh profile when returning from edit
    }

    private void loadProfile() {

        if (auth.getCurrentUser() == null) {
            Toast.makeText(requireContext(),
                    "User not logged in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String freelancerId = auth.getCurrentUser().getUid();

        profileHelper.getFreelancerProfile(freelancerId, task -> {

            if (task.isSuccessful() && task.getResult() != null) {

                DocumentSnapshot doc = task.getResult();
                FreelancerModel freelancer =
                        doc.toObject(FreelancerModel.class);

                if (freelancer != null) {
                    txtName.setText(freelancer.getName());
                    txtEmail.setText(freelancer.getEmail());
                    txtExperience.setText(
                            String.valueOf(freelancer.getExperienceYears())
                    );
                    txtHourlyRate.setText(
                            "$" + freelancer.getHourlyRate()
                    );
                    txtSkills.setText(freelancer.getSkills());
                }

            } else {
                Toast.makeText(requireContext(),
                        "Failed to load profile",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

