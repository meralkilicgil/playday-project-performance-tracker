package com.playday.jiratracker;

import java.util.List;

public class UserProfile {

    private String id;
    private String userName;
    private int totalActivity;
    private List<String> assignedIssues;

    public UserProfile(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalActivity() {
        return totalActivity;
    }

    public void setTotalActivity(int totalActivity) {
        this.totalActivity = totalActivity;
    }

    public List<String> getAssignedIssues() {
        return assignedIssues;
    }

    public void setAssignedIssues(List<String> assignedIssues) {
        this.assignedIssues = assignedIssues;
    }
}
