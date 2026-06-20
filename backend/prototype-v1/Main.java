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

//Additions - Post EMAIL : 
class FieldSchema {

    String name;
    String type;

    FieldSchema(String name, String type) {
        this.name = name;
        this.type = type;
    }
}

class FormSchema {

    String formId;
    String sourceUrl;
    String action;
    String method;

    ArrayList<FieldSchema> fields =
            new ArrayList<>();
}
class TransitionSchema {

    String from;
    String to;
    String method;
    String trigger;
}

public class Main {

    static HashSet<String> visited = new HashSet<>();

    static ArrayList<String> graphEdges = new ArrayList<>();

    static ArrayList<TransitionSchema> transitions =new ArrayList<>();

    static final int MAX_DEPTH = 1;

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Target URL: ");

        String targetUrl = scanner.nextLine();

        explore(targetUrl, 0);


        System.out.println("\nGenerated Graph Edges:");

        for (String edge : graphEdges) {
            System.out.println(edge);
        }
	System.out.println("\nGenerated Graph Edges:");

for (String edge : graphEdges) {
    System.out.println(edge);
}

System.out.println("\nTransitions:");

for (TransitionSchema t : transitions) {

    System.out.println(
            t.from + " -> " +
            t.to + " [" +
            t.trigger + "]"
    );
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
    FormSchema formSchema = new FormSchema();

formSchema.sourceUrl = url;
formSchema.action = action;
formSchema.method = method;
    Elements inputs = form.select("input");

    for (Element input : inputs) {

        String name = input.attr("name");
        String type = input.attr("type");

        System.out.println(
                "Input: " + name +
                " Type: " + type
        );
    }

}	


        for (Element link : links) {

            String extractedUrl = link.absUrl("href");

            System.out.println("Discovered: " + extractedUrl);

            if (!extractedUrl.isEmpty()
                    && extractedUrl.startsWith("http")) {
		    TransitionSchema t = new TransitionSchema();

t.from = url;
t.to = extractedUrl;
t.method = "GET";
t.trigger = "link_click";

transitions.add(t);

                String safeParent = url.replace("\"", "");
                String safeChild = extractedUrl.replace("\"", "");

                graphEdges.add("\"" + safeParent + "\" -> \"" + safeChild + "\";");

                explore(extractedUrl, depth + 1);
            }
        }
    }
}
