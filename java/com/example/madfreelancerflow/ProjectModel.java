package com.example.madfreelancerflow;

public class ProjectModel {

    private String freelancerName;
    private String name;
    private String duration;
    private Long completionPercentage;
    private String deadline;

    public ProjectModel() {
        // Required empty constructor
    }

    // ✅ GETTERS
    public String getFreelancerName() {
        return freelancerName != null ? freelancerName : "";
    }

    public String getName() {
        return name != null ? name : "";
    }

    public String getDuration() {
        return duration != null ? duration : "";
    }

    public int getCompletionPercentage() {
        return completionPercentage != null ? completionPercentage.intValue() : 0;
    }

    public String getDeadline() {
        return deadline != null ? deadline : "";
    }

    // ✅ SETTERS (VERY IMPORTANT 🔥)
    public void setFreelancerName(String freelancerName) {
        this.freelancerName = freelancerName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setCompletionPercentage(Long completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
