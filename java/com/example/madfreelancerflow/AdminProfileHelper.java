package com.example.madfreelancerflow;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminProfileHelper {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // ✅ Create a new admin profile
    public void createAdminProfile(AdminModel admin, OnCompleteListener<Void> listener) {
        db.collection("users")
                .document(admin.getAdminId())
                .set(admin)
                .addOnCompleteListener(listener);
    }

    // ✅ Get an admin profile by ID
    public void getAdminProfile(String adminId, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("users")
                .document(adminId)
                .get()
                .addOnCompleteListener(listener);
    }

    // ✅ Update admin profile fields (full update: name + phone)
    public void updateAdminProfile(String adminId, String name, String phoneNumber,
                                   OnCompleteListener<Void> listener) {
        db.collection("users")
                .document(adminId)
                .update(
                        "name", name,
                        "phoneNumber", phoneNumber
                )
                .addOnCompleteListener(listener);
    }

    // ✅ Update a single field dynamically (for long press edits)
    public void updateAdminProfileField(String adminId, String field, String newValue,
                                        OnCompleteListener<Void> listener) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(field, newValue);

        db.collection("users")
                .document(adminId)
                .update(updateMap)
                .addOnCompleteListener(listener);
    }

    // ✅ Update multiple fields dynamically
    public void updateAdminProfileFields(String adminId, Map<String, Object> fields,
                                         OnCompleteListener<Void> listener) {
        db.collection("users")
                .document(adminId)
                .update(fields)
                .addOnCompleteListener(listener);
    }

    // ✅ Update last login timestamp
    public void updateLastLogin(String adminId, long lastLogin, OnCompleteListener<Void> listener) {
        db.collection("users")
                .document(adminId)
                .update("lastLogin", lastLogin)
                .addOnCompleteListener(listener);
    }

    // ✅ Delete admin profile
    public void deleteAdminProfile(String adminId, OnCompleteListener<Void> listener) {
        db.collection("users")
                .document(adminId)
                .delete()
                .addOnCompleteListener(listener);
    }
}
