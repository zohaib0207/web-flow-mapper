import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class GeminiClient {

    private String apiKey;

    public GeminiClient() throws IOException {
        Properties properties = new Properties();

        FileInputStream input = new FileInputStream("config.properties");
        properties.load(input);

        apiKey = properties.getProperty("GEMINI_API_KEY");

        input.close();
    }

    public String getApiKey() {
        return apiKey;
    }
public String generateContent(String prompt) throws Exception {
	
prompt = prompt
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n");

    String endpoint =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                    + apiKey;

    String requestBody = """
    {
      "contents": [
        {
          "parts": [
            {
              "text": "%s"
            }
          ]
        }
      ]
    }
   """.formatted(prompt);

	HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(java.time.Duration.ofSeconds(20))
        .build();

 	HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpoint))
        .timeout(java.time.Duration.ofSeconds(30))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

	System.out.println("===== REQUEST BODY =====");
System.out.println(requestBody);

HttpResponse<String> response =
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

System.out.println("HTTP Status: " + response.statusCode());

ObjectMapper mapper = new ObjectMapper();
JsonNode root = mapper.readTree(response.body());

String report = root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

return report;


}
}
