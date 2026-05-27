import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    // Stores already visited URLs
    static HashSet<String> visited = new HashSet<>();
    static final int MAX_DEPTH = 2;

    public static void main(String[] args) throws Exception {

        explore("https://example.com", 0);
    }

    public static void explore(String url,int depth) throws Exception {

        // Skip already visited URLs
	if (depth > MAX_DEPTH) {
    return;
}
        if (visited.contains(url)) {
            return;
        }

        // Mark URL as visited
        visited.add(url);

        System.out.println("\nDepth: " + depth);
	System.out.println("Visiting: " + url);

        // Create HTTP Client
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        // Create Request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Send Request
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print Status Code
        System.out.println("Status Code: " + response.statusCode());

        // Parse HTML Response
        Document doc = Jsoup.parse(response.body(), url);

        // Extract all links
        Elements links = doc.select("a[href]");

        // Print discovered URLs
        for (Element link : links) {

            String extractedUrl = link.absUrl("href");

            System.out.println("Discovered: " + extractedUrl);
	    explore(extractedUrl,depth+1);
        }
    }
}
