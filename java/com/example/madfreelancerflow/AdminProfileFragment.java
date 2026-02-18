package com.example.madfreelancerflow;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminProfileFragment extends Fragment {

    private TextView txtName, txtEmail, txtPhone, txtCreatedAt;
    private Button btnEditProfile, btnLogout;

    private AdminProfileHelper helper;
    private String adminId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        txtName = view.findViewById(R.id.txt_name);
        txtEmail = view.findViewById(R.id.txt_email);
        txtPhone = view.findViewById(R.id.txt_phone);
        txtCreatedAt = view.findViewById(R.id.txt_created_at);

        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_edit_logout);

        helper = new AdminProfileHelper();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadProfile();

        // 🔥 LONG PRESS EDIT
        setLongPress(txtName, "name");
        setLongPress(txtPhone, "phoneNumber");
        setLongPress(txtEmail, "email");

        // 🔥 BUTTON EDIT PROFILE (OPEN ACTIVITY)
        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AdminEditProfileActivity.class));
        });

        // 🔥 LOGOUT
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void loadProfile() {
        helper.getAdminProfile(adminId, task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {

                AdminModel admin = task.getResult().toObject(AdminModel.class);

                if (admin != null) {
                    txtName.setText(admin.getName());
                    txtEmail.setText(admin.getEmail());
                    txtPhone.setText(admin.getPhoneNumber());

                    String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            .format(new Date(admin.getCreatedAt()));

                    txtCreatedAt.setText(date);
                }

            } else {
                Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLongPress(TextView view, String field) {
        view.setOnLongClickListener(v -> {

            PopupMenu popup = new PopupMenu(getContext(), view);
            popup.getMenu().add("Edit");

            popup.setOnMenuItemClickListener(item -> {
                showEditDialog(field, view);
                return true;
            });

            popup.show();
            return true;
        });
    }

    private void showEditDialog(String field, TextView targetView) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit " + field);

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(targetView.getText().toString());

        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {

            String newValue = input.getText().toString().trim();

            if (newValue.isEmpty()) return;

            helper.updateAdminProfileField(adminId, field, newValue, task -> {

                if (task.isSuccessful()) {
                    targetView.setText(newValue);
                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // 🔥 AUTO REFRESH WHEN RETURN FROM EDIT SCREEN
    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }
}
