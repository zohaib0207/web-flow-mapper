import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    static HashSet<String> visited = new HashSet<>();

    static ArrayList<String> graphEdges = new ArrayList<>();

    static final int MAX_DEPTH = 0;

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Target URL: ");

        String targetUrl = scanner.nextLine();

        explore(targetUrl, 0);

        System.out.println("\nGenerated Graph Edges:");

        for (String edge : graphEdges) {
            System.out.println(edge);
        }

        PrintWriter writer = new PrintWriter("graph.dot");

        writer.println("digraph G {");

        for (String edge : graphEdges) {
            writer.println(edge);
        }

        writer.println("}");

        writer.close();

        System.out.println("\nDOT file generated successfully.");
    }

    public static void explore(String url, int depth) throws Exception {

        if (depth > MAX_DEPTH) {
            return;
        }

        if (visited.contains(url)) {
            return;
        }

        visited.add(url);

        System.out.println("\nDepth: " + depth);
        System.out.println("Visiting: " + url);

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());

        Document doc = Jsoup.parse(response.body(), url);

        Elements links = doc.select("a[href]");
Elements forms = doc.select("form");

for (Element form : forms) {

    String action = form.attr("action");
    String method = form.attr("method");

    System.out.println("\nForm Discovered:");
    System.out.println("Action: " + action);
    System.out.println("Method: " + method);
}	


        for (Element link : links) {

            String extractedUrl = link.absUrl("href");

            System.out.println("Discovered: " + extractedUrl);

            if (!extractedUrl.isEmpty()
                    && extractedUrl.startsWith("http")) {

                String safeParent = url.replace("\"", "");
                String safeChild = extractedUrl.replace("\"", "");

                graphEdges.add("\"" + safeParent + "\" -> \"" + safeChild + "\";");

                explore(extractedUrl, depth + 1);
            }
        }
    }
}
