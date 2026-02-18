package com.example.madfreelancerflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminHomefragment extends Fragment {

    private TextView txtAdminName, txtFreelancerCount, txtActionCount;
    private LinearLayout pendingContainer;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        txtAdminName = view.findViewById(R.id.txt_admin_name);
        txtFreelancerCount = view.findViewById(R.id.txt_freelancer_count);
        txtActionCount = view.findViewById(R.id.txt_action_count);
        pendingContainer = view.findViewById(R.id.pending_freelancers_container);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadDashboardData();

        return view;
    }

    private void loadDashboardData() {

        if (auth.getCurrentUser() == null) return;
        String currentUserId = auth.getCurrentUser().getUid();

        // ✅ Admin Name
        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String name = document.getString("name");
                        txtAdminName.setText(name != null ? name : "Admin");
                    }
                });

        // ✅ ✅ ONLY APPROVED FREELANCERS COUNT
        db.collection("users")
                .whereEqualTo("role", "freelancer")
                .whereEqualTo("verification", "approved") // 🔥 FIX
                .get()
                .addOnSuccessListener(query ->
                        txtFreelancerCount.setText(String.valueOf(query.size()))
                );

        // ✅ ONLY PENDING USERS
        db.collection("users")
                .whereEqualTo("verification", "pending")
                .get()
                .addOnSuccessListener(query -> {

                    txtActionCount.setText(String.valueOf(query.size()));

                    pendingContainer.removeAllViews();

                    if (query.isEmpty()) {
                        TextView empty = new TextView(getContext());
                        empty.setText("No pending freelancers");
                        empty.setTextColor(0xFFFFFFFF);
                        empty.setPadding(16,16,16,16);
                        pendingContainer.addView(empty);
                        return;
                    }

                    for (QueryDocumentSnapshot doc : query) {

                        String rawName = doc.getString("name");
                        String userId = doc.getId();

                        final String name = (rawName == null || rawName.trim().isEmpty())
                                ? "No Name"
                                : rawName;

                        // Layout
                        LinearLayout itemLayout = new LinearLayout(getContext());
                        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                        itemLayout.setPadding(24, 24, 24, 24);
                        itemLayout.setBackgroundColor(0xFF252B3D);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 12, 0, 0);
                        itemLayout.setLayoutParams(params);

                        // Name
                        TextView txtName = new TextView(getContext());
                        txtName.setText(name);
                        txtName.setTextColor(0xFFFFFFFF);
                        txtName.setTextSize(15f);

                        LinearLayout.LayoutParams nameParams =
                                new LinearLayout.LayoutParams(0,
                                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                        txtName.setLayoutParams(nameParams);

                        // Status
                        TextView txtStatus = new TextView(getContext());
                        txtStatus.setText("Pending");
                        txtStatus.setTextColor(0xFFFF5722);
                        txtStatus.setTextSize(14f);

                        itemLayout.addView(txtName);
                        itemLayout.addView(txtStatus);

                        itemLayout.setOnClickListener(v ->
                                openPendingDialog(userId, name)
                        );

                        pendingContainer.addView(itemLayout);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Error loading data", Toast.LENGTH_SHORT).show()
                );
    }

    private void openPendingDialog(String userId, String userName) {
        new AlertDialog.Builder(getContext())
                .setTitle("Verify Freelancer")
                .setMessage("Approve or Reject " + userName + "?")
                .setPositiveButton("Approve", (dialog, which) ->
                        updateVerification(userId, "approved"))
                .setNegativeButton("Reject", (dialog, which) ->
                        updateVerification(userId, "rejected"))
                .show();
    }

    private void updateVerification(String userId, String status) {

        DocumentReference docRef = db.collection("users").document(userId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("verification", status);

        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(),
                            "Freelancer " + status, Toast.LENGTH_SHORT).show();

                    loadDashboardData(); // 🔁 refresh
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Update failed", Toast.LENGTH_SHORT).show()
                );
    }
}
