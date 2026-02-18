package com.example.madfreelancerflow;

import android.util.Log;

import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class ProjectHelper {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String COLLECTION = "projects";

    public interface ProjectCallback {
        void onSuccess(List<ProjectModel> projectList);
        void onFailure(Exception e);
    }

    public void getProjects(ProjectCallback callback) {

        db.collection(COLLECTION)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<ProjectModel> list = new ArrayList<>();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        // 🔥 Convert document → model
                        ProjectModel project = doc.toObject(ProjectModel.class);

                        if (project != null) {
                            list.add(project);

                            // 🔥 DEBUG LOG (VERY IMPORTANT)
                            Log.d("FIREBASE_DATA",
                                    "Project: " + project.getName() +
                                            " | Freelancer: " + project.getFreelancerName());
                        }
                    }

                    callback.onSuccess(list);
                })
                .addOnFailureListener(e -> {
                    Log.e("FIREBASE_ERROR", "Error fetching projects", e);
                    callback.onFailure(e);
                });
    }
}
