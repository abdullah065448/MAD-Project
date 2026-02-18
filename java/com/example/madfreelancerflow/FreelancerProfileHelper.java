package com.example.madfreelancerflow;

import com.example.madfreelancerflow.FreelancerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.*;

public class FreelancerProfileHelper {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createFreelancerProfile(FreelancerModel freelancer,
                                        OnCompleteListener<Void> listener) {
        freelancer.setVerified(false);
        db.collection("Freelancer")
                .document(freelancer.getFreelancerId())
                .set(freelancer)
                .addOnCompleteListener(listener);
    }

    public void getFreelancerProfile(String freelancerId,
                                     OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("Freelancer")
                .document(freelancerId)
                .get()
                .addOnCompleteListener(listener);
    }

    public void updateFreelancerProfile(String freelancerId,
                                        String bio,
                                        String skills,
                                        double hourlyRate,
                                        int experienceYears,
                                        String portfolioUrl,
                                        String country,
                                        OnCompleteListener<Void> listener) {

        db.collection("Freelancer")
                .document(freelancerId)
                .update(
                        "bio", bio,
                        "skills", skills,
                        "hourlyRate", hourlyRate,
                        "experienceYears", experienceYears,
                        "portfolioUrl", portfolioUrl,
                        "country", country
                )
                .addOnCompleteListener(listener);
    }

    public void updateFreelancerRating(String freelancerId,
                                       float rating,
                                       OnCompleteListener<Void> listener) {
        db.collection("Freelancer")
                .document(freelancerId)
                .update("rating", rating)
                .addOnCompleteListener(listener);
    }

    public void deleteFreelancerProfile(String freelancerId,
                                        OnCompleteListener<Void> listener) {
        db.collection("Freelancer")
                .document(freelancerId)
                .delete()
                .addOnCompleteListener(listener);
    }
}
