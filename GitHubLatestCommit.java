import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GitHubLatestCommit {
    public static void main(String[] args) {
        String username = "jersha";
        String repo = "javatask";
        String committer = "jersha";
        
        try {
            String latestCommit = getLatestCommit(username, repo, committer);
            System.out.println(latestCommit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLatestCommit(String username, String repo, String committer) throws Exception {
        String url = String.format("https://github.com/jersha4/javatask", username, repo);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            return "Error: Unable to fetch data from GitHub API. Status code: " + response.statusCode();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode commits = objectMapper.readTree(response.body());

        for (JsonNode commit : commits) {
            String commitCommitter = commit.get("commit").get("committer").get("name").asText();
            if (commitCommitter.equalsIgnoreCase(committer)) {
                String sha = commit.get("sha").asText();
                String commitTimestamp = commit.get("commit").get("committer").get("date").asText();
                return String.format("{\n\t\"sha\": \"%s\",\n\t\"commit_timestamp\": \"%s\",\n\t\"committer\": \"%s\"\n}", 
                                     sha, commitTimestamp, commitCommitter);
            }
        }

        return "No commit found for the specified committer.";
    }
}
Instructions to Run the Script:
Add the Jackson library for JSON parsing to your project. If you're using Maven, add the following dependencies to your pom.xml:

xml
Copy code
<dependencies>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
</dependencies>