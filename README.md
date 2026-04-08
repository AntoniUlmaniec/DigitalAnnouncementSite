# Digital Announcement Site

## About the Project
**Digital Announcement Site** is a full-stack web application serving as a digital notice board. It was developed as a final academic project for the "High-Level Programming Languages" course. The application allows users to view, post, and manage announcements dynamically.

The backend is built using **Java** with the lightweight **Spark Framework** for REST API routing and **Gson** for efficient JSON data handling. The frontend is implemented using vanilla JavaScript, HTML, and CSS, communicating seamlessly with the Java backend.

## Key Features
* **Digital Notice Board:** A platform for users to view and publish announcements.
* **REST API Backend:** Built with Java (Spark framework) to handle requests, logic, and data processing.
* **Structured Data:** Uses Gson for serialization and deserialization of JSON objects.
* **Full-Stack Architecture:** Clear separation of concerns—`.java` source files are located in `src/main/java`, while static frontend assets (HTML, JS, CSS) reside in `src/main/resources`.
* **Project Management:** Built using Maven (`pom.xml`) for easy dependency management.

## Tech Stack
* **Backend:** Java, Spark Framework, Gson, Maven
* **Frontend:** JavaScript, HTML5, CSS3
* **System Design:** UML diagrams (PlantUML files included in the repository)

## How to Run

1. **Install Dependencies:**
   Ensure you have Maven and a Java IDE (like IntelliJ IDEA or Eclipse) installed. The required libraries (Spark, Gson) will be downloaded automatically via the `pom.xml` configuration.

2. **Start the Server:**
   Locate the main `System` class in `src/main/java` and run it. 
   *(Note: Upon the first launch, your OS firewall may prompt you for permission. Please accept it.)*

3. **Access the Application:**
   The server runs locally on port `4000`. Open your preferred web browser and navigate to:
   `http://localhost:4000`
