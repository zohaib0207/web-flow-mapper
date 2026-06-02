# Web Flow Mapper

Web Flow Mapper is a behavioral web exploration and traversal analysis system designed to automatically explore web application paths, analyze traversal behavior, and generate visual flow representations of discovered application structures.

The project focuses on recursive traversal, behavioral relationship discovery, and graph-based visualization of web application navigation flows.

The long-term objective of the project is to evolve into a behavioral fuzzing and intelligent exploration framework capable of analyzing dynamic web application behavior.

# Current Functionality

The current prototype is capable of:

* Sending HTTP requests dynamically to target websites
* Parsing returned HTML responses
* Extracting hyperlinks automatically
* Recursively traversing discovered URLs
* Maintaining traversal state using visited URL tracking
* Preventing infinite recursive traversal using convergence control
* Generating behavioral traversal relationships
* Producing graph representations using Graphviz
* Accepting runtime target URLs dynamically

# Current Workflow

User Input URL

↓

HTTP Request Sent

↓

HTML Response Received

↓

HTML Parsed Using JSoup

↓

Hyperlinks Extracted

↓

Recursive Traversal Performed

↓

Traversal Relationships Generated

↓

DOT Graph Generated

↓

Graph Visualization Produced

# Technologies Used

Backend:

* Java
* Java HttpClient
* JSoup
* Graphviz

Frontend:

* Planned frontend visualization interface

# Current Project Architecture

1. Request Engine
   Responsible for dynamically sending HTTP requests to target websites and receiving responses.

2. HTML Parsing Engine
   Parses raw HTML responses into traversable DOM structures using JSoup.

3. URL Extraction Module
   Extracts hyperlinks dynamically from parsed HTML pages.

4. Recursive Traversal Engine
   Recursively explores discovered traversal paths automatically.

5. Traversal Convergence Controller
   Prevents infinite recursive exploration using:

* visited URL tracking
* traversal depth limitation

6. Behavioral Flow Mapper
   Converts traversal relationships into node-edge graph structures.

7. Graph Visualization Module
   Generates DOT graph files and renders graphical traversal representations using Graphviz.

# Current Features Implemented

* Dynamic runtime URL traversal
* Recursive behavioral exploration
* HTTP response handling
* Hyperlink extraction
* Depth-based traversal control
* Invalid URL scheme filtering
* Graph relationship generation
* DOT graph generation
* Graphviz visualization support

# Current Limitations

The current prototype does not yet support:

* JavaScript-rendered pages
* Form interaction
* POST request mutation
* Authentication/session handling
* Same-domain traversal restriction
* Intelligent traversal prioritization
* Payload mutation fuzzing

# Example Testing Websites

Recommended websites for testing:

https://example.com

https://books.toscrape.com

http://localhost:8080 (DVWA local environment)

# How To Run

Compile:

javac -cp jsoup-1.17.2.jar Main.java

Run:

java -cp .:jsoup-1.17.2.jar Main

Enter target URL when prompted.

Example:

https://example.com

# Generate Graph Visualization

After traversal completes:

dot -Tpng graph.dot -o graph.png

Open generated graph:

xdg-open graph.png

# Current Development Status

The current implementation successfully demonstrates recursive behavioral traversal and graphical flow mapping of real websites.

The backend traversal foundation has been implemented successfully and future development will focus on:

* behavioral fuzzing
* intelligent traversal
* session-aware exploration
* frontend visualization integration
* interactive flow analysis
