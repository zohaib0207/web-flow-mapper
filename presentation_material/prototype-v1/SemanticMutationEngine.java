import java.util.ArrayList;

public class SemanticMutationEngine {

    public static ArrayList<String> generateMutations(FieldSchema field) {

        ArrayList<String> payloads = new ArrayList<>();

        String type = field.type.toLowerCase();
        String name = field.name.toLowerCase();

        if (type.equals("password")) {

            payloads.add("password123");
            payloads.add("123456");
            payloads.add("Password@123");
            payloads.add("' OR 1=1 --");
            payloads.add("AAAAAAAAAAAAAAAAAAAA");

        } else if (type.equals("email") || name.contains("email")) {

            payloads.add("test@example.com");
            payloads.add("admin@example.com");
            payloads.add("invalid@email");

        } else if (type.equals("number")) {

            payloads.add("0");
            payloads.add("-1");
            payloads.add("999999999");

        } else if (type.equals("text")) {

            payloads.add("test");
            payloads.add("<script>alert(1)</script>");
            payloads.add("' OR '1'='1");

        } else {

            payloads.add("sample");
        }

        return payloads;
    }
}