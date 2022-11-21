package com.playday.jiratracker;

public class jirafilter {
    private String projectname;
    private String sprintname;
    private String releaseversion;

    public String getProjectName() {
        return projectname;
    }

    public void setProjectName(String projectName) {
        this.projectname = projectName;
    }

    public String getSprintName() {
        return sprintname;
    }

    public void setSprintName(String sprintName) {
        this.sprintname = sprintName;
    }

    public String getReleaseVersion() {
        return releaseversion;
    }

    public void setReleaseVersion(String releaseVersion) {
        this.releaseversion = releaseVersion;
    }


}
