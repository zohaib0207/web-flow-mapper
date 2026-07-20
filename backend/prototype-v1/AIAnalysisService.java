import java.nio.file.Files;
import java.nio.file.Paths;

public class AIAnalysisService {

public static void generateAnalysis() throws Exception {

String states = Files.readString(Paths.get("json/states.json"));
states = states.substring(0, Math.min(states.length(), 2000));

String forms = Files.readString(Paths.get("json/forms.json"));
forms = forms.substring(0, Math.min(forms.length(), 1000));

String transitions = Files.readString(Paths.get("json/transitions.json"));
transitions = transitions.substring(0, Math.min(transitions.length(), 2000));
    GeminiClient client = new GeminiClient();

	String prompt = """
You are an assistant that explains website structure and potential security observations in simple English.

Below is the output of my website crawler.

States:
%s

Forms:
%s

Transitions:
%s

Using ONLY this information, generate a report with the following sections:

1. Website Overview
- What kind of website is this?
- What pages or sections were discovered?

2. Forms Found
- List important forms.
- Explain what each form is probably used for.

3. Authentication
- Did you find any login forms or authentication pages?
- Explain what was discovered.

4. Navigation Summary
- Explain how the crawler moved through the website.
- Mention important transitions.

5. Interesting Areas
- Which pages would be worth investigating further?

6. Possible Security Observations
- Mention possible attack surfaces.
- Do NOT claim vulnerabilities unless the crawl actually suggests them.
- Clearly separate observations from assumptions.

7. Explain Like I'm New
- Explain in simple language what this crawl tells us.
- Why is this useful for a penetration tester or security researcher?

Use only the supplied crawl data.
Do not invent pages or vulnerabilities.
""".formatted(states, forms, transitions);

String response = client.generateContent(prompt);
    System.out.println(response);
	//System.out.println(states);
}

}
