import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
//Additions - Post EMAIL :
class FieldSchema {

    public  String name;
    public  String type;

    FieldSchema(String name, String type) {
        this.name = name;
        this.type = type;
    }
}

class StateSchema {

    public String stateId;
    public String url;

    // Crawl metadata
    public int depth;
    public int statusCode;

    // Authentication metadata
    public boolean hasLoginForm = false;
    public boolean authenticationRequired = false;

    // Future fuzzing metadata
    public boolean discoveredByMutation = false;
    public boolean isUnknownState = false;
    public String discoveryReason = "";

    // Time discovered
    public String timestamp;

    public ArrayList<FormSchema> forms =
        new ArrayList<>();

    public ArrayList<String> links =
        new ArrayList<>();
}

class FormSchema {

    public  String formId;
    public  String sourceUrl;
    public String action;
    public  String method;

    ArrayList<FieldSchema> fields =
        new ArrayList<>();
}

class TransitionSchema {

    public String transitionId;

    public String from;
    public String to;

    public String method;
    public String trigger;

    public int depth;

    public boolean discoveredByMutation = false;

    // Coverage-guided priority
    public int priority = 0;

    // Reason why this transition received the score
    public String priorityReason = "";
}

public class Main {

    static HashSet<String> visited = new HashSet<>();

    static ArrayList<String> graphEdges = new ArrayList<>();

    static ArrayList<TransitionSchema> transitions =new ArrayList<>();
	
    static ArrayList<StateSchema> states = new ArrayList<>();

    static int transitionCounter = 0;

	static int stateCounter = 0;

    static int formCounter = 0;

    static ArrayList<FormSchema> discoveredForms = new ArrayList<>();

    static final int MAX_DEPTH = 1;




    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Target URL: ");

        String targetUrl = scanner.nextLine();
        String timestamp =
            LocalDateTime.now()
            .format(
                DateTimeFormatter.ofPattern(
                    "yyyyMMdd_HHmmss"
                )
            );

        String dotFile =
            "graph_" + timestamp + ".dot";

        String pngFile =
            "graph_" + timestamp + ".png";

        explore(targetUrl, 0);
        ObjectMapper mapper = new ObjectMapper();

        mapper.writerWithDefaultPrettyPrinter()
        .writeValue(new File("json/forms.json"), discoveredForms);
	mapper.writerWithDefaultPrettyPrinter()
      .writeValue(new File("json/states.json"), states);
        mapper.writerWithDefaultPrettyPrinter()
        .writeValue(new File("json/transitions.json"),
                    transitions);

        System.out.println("\nGenerated Graph Edges:");
        for (TransitionSchema t : transitions) {

            System.out.println(
                t.from + " -> " +
                t.to + " [" +
                t.trigger + "]"
            );
        }

        for (String edge : graphEdges) {
            System.out.println(edge);
        }
        System.out.println("\nGenerated Graph Edges:");

        for (String edge : graphEdges) {
            System.out.println(edge);
        }

        System.out.println("\nTransitions:");

        PrintWriter writer = new PrintWriter(dotFile);

        writer.println("digraph G {");

        for (String edge : graphEdges) {
            writer.println(edge);
        }

        writer.println("}");

        writer.close();

        System.out.println("\nDOT file generated successfully.");
        
        scanner.close();
    }

    public static void explore(String url, int depth) throws Exception {

        if (depth > MAX_DEPTH) {
            return;
        }

        boolean firstVisit = !visited.contains(url);

        if (!firstVisit) {
            return;
        }

        visited.add(url);

        

        System.out.println("\nDepth: " + depth);
        System.out.println("Visiting: " + url);
	StateSchema state = new StateSchema();

	
	state.stateId = "S" + stateCounter++;
    state.url=url;
    state.depth = depth;
   

    state.timestamp =
        LocalDateTime.now()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        HttpClient client = HttpClient.newBuilder()
                            .followRedirects(HttpClient.Redirect.NEVER)
                            .build();

        HttpRequest request = HttpRequest.newBuilder()
                              .uri(URI.create(url))
                              .GET()
                              .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        state.statusCode = response.statusCode();

        state.isUnknownState =
            UnknownStateDetector.isUnknown(firstVisit);

        state.discoveryReason =
            UnknownStateDetector.determineReason(
                state,
                response.statusCode(),
                firstVisit
            );

        System.out.println("Status Code: " + response.statusCode());

        Document doc = Jsoup.parse(response.body(), url);
        // ---------- Authentication Detection ----------

        String lowerUrl = url.toLowerCase();

        if (response.statusCode() == 401 ||
            response.statusCode() == 403 ||
            lowerUrl.contains("login") ||
            lowerUrl.contains("signin") ||
            lowerUrl.contains("auth")) {

            state.authenticationRequired = true;

            System.out.println("[AUTH] Authentication Required");
        }

        Elements links = doc.select("a[href]");
        Elements forms = doc.select("form");

        for (Element form : forms) {

            String action = form.attr("action");
            String method = form.attr("method");
            if (isLoginForm(form)) {

                state.hasLoginForm = true;
                state.authenticationRequired = true;
                System.out.println("[LOGIN] Login Form Detected");
            }

            System.out.println("\nForm Discovered:");
            System.out.println("Action: " + action);
            System.out.println("Method: " + method);
            FormSchema formSchema = new FormSchema();
            formSchema.formId = "F" + formCounter++;

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
                FieldSchema field = new FieldSchema(name, type);
                ArrayList<String> mutations =
                        SemanticMutationEngine.generateMutations(field);

                System.out.println("Generated Mutations:");

                for(String mutation : mutations){

                    System.out.println("   -> " + mutation);
                }
                formSchema.fields.add(field);
            }
            discoveredForms.add(formSchema);
	    state.forms.add(formSchema);
        }
        // Determine why this state was discovered

        if (state.authenticationRequired) {

            state.discoveryReason = "Authentication State";

        } else if (state.hasLoginForm) {

            state.discoveryReason = "Login Form State";

        } else if (state.isUnknownState) {

            state.discoveryReason = "Previously Unknown State";
        }


        for (Element link : links) {

            String extractedUrl = link.absUrl("href");

            System.out.println("Discovered: " + extractedUrl);


            if (!extractedUrl.isEmpty()
                    && extractedUrl.startsWith("http")) {
                TransitionSchema t = new TransitionSchema();    
                t.transitionId = "T" + transitionCounter++;
                t.depth = depth;

                t.from = url;
                t.to = extractedUrl;
                t.method = "GET";
                t.trigger = "link_click";

                t.priority = CoveragePrioritizer.calculatePriority(extractedUrl);

                t.priorityReason =
                    CoveragePrioritizer.getPriorityReason(t.priority);


                transitions.add(t);
                System.out.println(
                    "[Coverage] Priority Score = "+ t.priority +
                    " | " + t.priorityReason
               );
		state.links.add(extractedUrl);

                String safeParent = url.replace("\"", "");
                String safeChild = extractedUrl.replace("\"", "");

                graphEdges.add("\"" + safeParent + "\" -> \"" + safeChild + "\";");

                System.out.println("Priority: " + t.priority);

                explore(extractedUrl, depth + 1);

            }
        }
	states.add(state);

    System.out.println(
        "[STATE] " + state.stateId +
        " | Unknown: " + state.isUnknownState +
        " | Reason: " + state.discoveryReason
    );

    }

    private static boolean isLoginForm(Element form) {

        Elements inputs = form.select("input");

        boolean hasPassword = false;
        boolean hasUsername = false;

        for (Element input : inputs) {

            String name = input.attr("name").toLowerCase();
            String type = input.attr("type").toLowerCase();

            if (type.equals("password")) {
                hasPassword = true;
            }

            if (name.contains("user")
                    || name.contains("email")
                    || name.contains("login")) {
                hasUsername = true;
            }
        }
        return hasPassword && hasUsername;
}

private static int calculatePriority(String url) {

    int score = 0;

    String lower = url.toLowerCase();

    if (lower.contains("login") ||
        lower.contains("signin") ||
        lower.contains("auth")) {

        score += 5;
    }

    if (lower.contains("register")) {

        score += 4;
    }

    if (lower.contains("search")) {

        score += 3;
    }

    if (lower.contains("admin")) {

        score += 5;
    }
    if(score == 0){
        score = 1;
    }

    return score;
}

   
}
