package com.example.madfreelancerflow;

public class FreelancerModel {

    private String freelancerId;
    private String name;
    private String email;
    private String bio;
    private String skills;
    private double hourlyRate;
    private int experienceYears;
    private String portfolioUrl;
    private String country;
    private boolean verified;
    private float rating;
    private long createdAt;

    public FreelancerModel() {}

    public FreelancerModel(String freelancerId, String name, String email,
                           String bio, String skills, double hourlyRate,
                           int experienceYears, String portfolioUrl,
                           String country, boolean verified,
                           float rating, long createdAt) {
        this.freelancerId = freelancerId;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.skills = skills;
        this.hourlyRate = hourlyRate;
        this.experienceYears = experienceYears;
        this.portfolioUrl = portfolioUrl;
        this.country = country;
        this.verified = verified;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getFreelancerId() { return freelancerId; }
    public void setFreelancerId(String freelancerId) { this.freelancerId = freelancerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }

    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
