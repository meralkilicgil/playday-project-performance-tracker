package com.playday.jiratracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    public JiraProjectService jiraProjectService;

    @GetMapping("/results")
    public String resultPage(Model model) {
        return "results";
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            String projectName = "SDRIVE";
            Map<String, String> versions = jiraProjectService.getReleases(projectName);

            ArrayList<String> versionNames = new ArrayList<>();

            for (String e : versions.values()) {
                versionNames.add(e);
            }
            model.addAttribute("releases", versionNames);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "index";
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam("projectName") String projectName, @RequestParam("release") String  release, Model model) {

        try {
            System.out.println("form submitted");
            StringBuilder jql = new StringBuilder("project=");
            jql.append("'");
            jql.append(projectName);
            jql.append("'");
            jql.append(" AND ");
            jql.append( "resolution = 'Resolved' AND statuscategory ='Complete'  AND fixVersion = '");
            jql.append(release);
            jql.append("'");
            jiraProjectService.getQuery(jql, projectName, release);
            Map<String, UserProfile> developmentTeam = jiraProjectService.developmentTeam;
            model.addAttribute("usernames", developmentTeam.keySet());
            List<UserProfile> userProfiles = new ArrayList<>();
            for(String pro: developmentTeam.keySet()) {
                userProfiles.add(developmentTeam.get(pro));
            }

            model.addAttribute("userprofiles", userProfiles);
            model.addAttribute("project", "Project Name: " + projectName);
            model.addAttribute("release", "Release Version: " + release);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "results";
    }
}
