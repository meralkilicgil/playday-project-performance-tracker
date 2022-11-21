package com.playday.jiratracker;

import java.math.BigInteger;
import java.util.List;

public class Issue {
    private String name;
    private String projectName;
    private String releaseVersion;
    private String priority;
    private String assignee;
    private String status;
    private String issueType;
    private String timeOriginalEstimate;
    private BigInteger timeSpent;
    private String reporter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getReleaseVersion() {
        return releaseVersion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseVersion = releaseVersion;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getTimeOriginalEstimate() {
        return timeOriginalEstimate;
    }

    public void setTimeOriginalEstimate(String timeOriginalEstimate) {
        this.timeOriginalEstimate = timeOriginalEstimate;
    }

    public BigInteger getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(BigInteger timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }
}
