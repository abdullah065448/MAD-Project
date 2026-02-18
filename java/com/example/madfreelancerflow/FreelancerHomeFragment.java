package com.example.madfreelancerflow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FreelancerHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProjectAdapter adapter;
    private List<ProjectModel> projectList;

    private TextView projectNum;
    private String loggedInUserEmail = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_freelancer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerProjects);
        projectNum = view.findViewById(R.id.projectNum);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        projectList = new ArrayList<>();
        adapter = new ProjectAdapter(projectList);
        recyclerView.setAdapter(adapter);

        // ✅ ALWAYS USE EMAIL (IMPORTANT FIX)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            loggedInUserEmail = user.getEmail().trim();
        }

        loadProjects();
    }

    private void loadProjects() {

        new ProjectHelper().getProjects(new ProjectHelper.ProjectCallback() {
            @Override
            public void onSuccess(List<ProjectModel> projects) {

                projectList.clear();
                int count = 0;

                for (ProjectModel project : projects) {

                    String dbEmail = project.getFreelancerName().trim();

                    // 🔥 DEBUG (check mismatch in Logcat)
                    Log.d("DEBUG", "DB Email: " + dbEmail);
                    Log.d("DEBUG", "User Email: " + loggedInUserEmail);

                    if (!dbEmail.isEmpty() &&
                            dbEmail.equalsIgnoreCase(loggedInUserEmail)) {

                        projectList.add(project);
                        count++;
                    }
                }

                projectNum.setText(count + " Projects");

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FIREBASE_ERROR", e.getMessage());
            }
        });
    }
}
