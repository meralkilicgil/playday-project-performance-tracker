package com.playday.jiratracker;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

@Service
public class JiraProjectService {

    private final String username ="mkilicgil@cyangate.com";
    private final String apiKey ="qSBGKx7Ero0OAjKfajvqC55C";
    public Map<String, UserProfile> developmentTeam = new HashMap<>();


    public JiraProjectService() {
        createDevTeam("5e6f655faab8250c443e1c6c", "Meral Kilicgil");
        createDevTeam("5bbc5a474a8a24512aa88e5c", "Gulsah Kirtiloglu");
        createDevTeam("5b4beb753675c92cbbdecdf1", "Cihan Tiambeng");
        createDevTeam("6125b5027a1bfb00717f1eb4", "Yagiz Gani");
        createDevTeam("5cfdb1239e4bc30bc3963d9b", "Orkun Alpar");
        createDevTeam("5ca8b2eb0aada6279e93852a", "Emin Civelek");
        createDevTeam("62c2b2f75f45f3d3b7b60147", "Aykan Ankara");
        createDevTeam("557058:a6b33426-bfbd-462a-8960-cd01ddcb9ecb", "Yasemin Doganci");
    }

    public Map<String, String> getReleases(String projectName) throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> projectVersions = new HashMap<>();
        String requestUrl = "https://cyangate.atlassian.net/rest/api/3/project/" + projectName + "/versions";
        String authHeader = createHeaders();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(requestUrl))
                .header("Authorization", authHeader)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String reponseStr = response.body().toString();
        JSONArray resArr = new JSONArray(reponseStr);

        for(Object obj: resArr) {
            JSONObject jsonObject = (JSONObject) obj;
            if(jsonObject.getInt("id") > 11747) {
                projectVersions.put(jsonObject.getString("id"), jsonObject.getString("name"));
            }
        }
        return projectVersions;
    }

    private String createHeaders(){
        String auth = username + ":" + apiKey;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        return authHeader;
    }

    public void getQuery(StringBuilder jql, String projectName, String releaseVersion) throws URISyntaxException, IOException, InterruptedException {
        String requestUrl = "https://cyangate.atlassian.net/rest/api/3/search?jql=" + URLEncoder.encode(jql.toString(), StandardCharsets.UTF_8);
        String authHeader = createHeaders();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(requestUrl))
                .header("Authorization", authHeader)
                .GET()
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject responseObj = new JSONObject(response.body().toString());
        resetActivity();
        JSONArray issues = responseObj.getJSONArray("issues");
        List<Issue> jqlResult = parseJQLResult(issues, projectName, releaseVersion);
        parseUserProfile(jqlResult);
    }

    public List<Issue> parseJQLResult(JSONArray jqlResult, String projectName, String releaseVersion) {
        List<Issue> issueList = new ArrayList<>();
        for(Object obj: jqlResult) {
            Issue issue = new Issue();
            issue.setProjectName(projectName);
            issue.setReleaseVersion(releaseVersion);
            JSONObject jsonObject = (JSONObject) obj;
            issue.setName(jsonObject.getString("key"));
            JSONObject fieldObject = jsonObject.getJSONObject("fields");
            if(fieldObject != null) {
                issue.setIssueType(fieldObject.get("issuetype") != null ? fieldObject.getJSONObject("issuetype").getString("name") : "");
                issue.setAssignee(fieldObject.get("assignee") != null ? fieldObject.getJSONObject("assignee").getString("displayName") : "");
                issue.setStatus(fieldObject.get("status") != null ? fieldObject.getJSONObject("status").getString("name") : "");
                issue.setPriority(fieldObject.get("priority") != null ? fieldObject.getJSONObject("priority").getString("name") : "LOW");
                issue.setReporter(fieldObject.get("reporter") != null ? fieldObject.getJSONObject("reporter").getString("displayName") : "");
            }

            issueList.add(issue);
        }
        return issueList;
    }

    public void parseUserProfile(List<Issue> issueList) {

        try {
            for(Issue issue: issueList) {
                String assignee = issue.getAssignee();
                if(developmentTeam.containsKey(assignee)) {
                    int activityCount = developmentTeam.get(assignee).getTotalActivity();
                    String priorityStr = issue.getPriority();
                    Integer priority = calculatePriority(priorityStr);
                    String reporter = issue.getReporter();
                    activityCount += priority;
                    UserProfile userProfile = developmentTeam.get(assignee);
                    if(developmentTeam.containsKey(reporter)) {
                        UserProfile reporterProfile = developmentTeam.get(reporter);
                        reporterProfile.setTotalActivity(reporterProfile.getTotalActivity()+1);
                        developmentTeam.put(reporterProfile.getUserName(), reporterProfile);
                    }

                    userProfile.setTotalActivity(activityCount);
                    userProfile.getAssignedIssues().add(issue.getName());
                    developmentTeam.put(userProfile.getUserName(), userProfile);


                }
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void createDevTeam(String id, String username) {
        UserProfile userProfile = new UserProfile(id, username);
        userProfile.setAssignedIssues(new ArrayList<String>());
        developmentTeam.put(username, userProfile);
    }

    public void resetActivity() {
        for(String pr: developmentTeam.keySet()) {
            developmentTeam.get(pr).setTotalActivity(0);
        }
    }

    public Integer calculatePriority(String prio) {
        switch (prio) {
            case "Blocker":
                return 5;
            case "High":
                return 4;
            case "Medium":
                return 3;
            case "Low":
                return 2;
            case "Lowest":
                return 1;
            default:
                return 1;
        }
    }
}
