package com.example.madfreelancerflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private List<ProjectModel> projectList;

    public ProjectAdapter(List<ProjectModel> projectList) {
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_projects, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProjectModel project = projectList.get(position);

        holder.tvName.setText(project.getName());
        holder.tvDuration.setText("Duration: " + project.getDuration());
        holder.tvDeadline.setText("Deadline: " + project.getDeadline());
        holder.progressBar.setProgress(project.getCompletionPercentage());
        holder.tvPercentage.setText(project.getCompletionPercentage() + "% Completed");
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDuration, tvDeadline, tvPercentage;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvProjectName);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvPercentage = itemView.findViewById(R.id.tvPercentage);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
