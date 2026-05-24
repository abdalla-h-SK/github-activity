package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GitHubActivity {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java GitHubActivity <username>");
            System.exit(1);
        }

        String username = args[0];
        String url = "https://api.github.com/users/" + username + "/events/public";

        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/vnd.github.v3+json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                System.out.println("Error: User '" + username + "' not found.");
                System.exit(1);
            } else if (response.statusCode() != 200) {
                System.out.println("Error fetching data. HTTP Status: " + response.statusCode());
                System.exit(1);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode events = mapper.readTree(response.body());

            int limit = events.size();

            if (args.length > 1) {
                try {
                    int requestedLimit = Integer.parseInt(args[1]);
                    if (requestedLimit > 0) {
                        limit = Math.min(requestedLimit, events.size());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid limit provided in args[1]. Defaulting to all recent events.");
                }
            }

            if (events.isEmpty()) {
                System.out.println("No recent activity found for " + username);
                return;
            }

            System.out.println("Output (Showing up to " + limit + " events):");

            for (int i = 0; i < limit; i++) {
                JsonNode event = events.get(i);
                String type = event.get("type").asText();
                String repoName = event.get("repo").get("name").asText();

                switch (type) {
                    case "PushEvent":
                        String branch = event.path("payload")
                                .path("ref")
                                .asText("")
                                .replace("refs/heads/", "");
                        System.out.println("- Pushed to " + repoName + " on branch " + branch);
                        break;
                    case "IssuesEvent":
                        String action = event.get("payload").get("action").asText();
                        System.out.println("- " + capitalize(action) + " an issue in " + repoName);
                        break;
                    case "WatchEvent":
                        System.out.println("- Starred " + repoName);
                        break;
                    case "CreateEvent":
                        String refType = event.get("payload").get("ref_type").asText();
                        System.out.println("- Created " + refType + " in " + repoName);
                        break;
                    case "PullRequestEvent":
                        String prAction = event.get("payload").get("action").asText();
                        System.out.println("- " + capitalize(prAction) + " a pull request in " + repoName);
                        break;
                    case "DeleteEvent":
                        // Usually triggered when a branch or tag is deleted
                        String delRefType = event.get("payload").get("ref_type").asText();
                        System.out.println("- Deleted " + delRefType + " in " + repoName);
                        break;
                    case "ForkEvent":
                        String forkedTo = event.get("payload").get("forkee").get("full_name").asText();
                        System.out.println("- Forked " + repoName + " to " + forkedTo);
                        break;
                    default:
                        System.out.println("- " + type + " in " + repoName);
                        break;
                }
            }

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}