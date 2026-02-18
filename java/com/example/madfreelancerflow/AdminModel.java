package com.example.madfreelancerflow;

public class AdminModel {

    private String adminId;
    private String name;
    private String email;
    private String phoneNumber;
    private String roleLevel;
    private boolean active;
    private long lastLogin;
    private long createdAt;

    public AdminModel() {}

    public AdminModel(String adminId, String name, String email,
                      String phoneNumber, String roleLevel,
                      boolean active, long lastLogin, long createdAt) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleLevel = roleLevel;
        this.active = active;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
    }

    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRoleLevel() { return roleLevel; }
    public void setRoleLevel(String roleLevel) { this.roleLevel = roleLevel; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public long getLastLogin() { return lastLogin; }
    public void setLastLogin(long lastLogin) { this.lastLogin = lastLogin; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
