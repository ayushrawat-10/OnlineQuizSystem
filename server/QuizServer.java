package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import dao.QuestionDAO;
import dao.ScoreDAO;
import model.Question;
import model.Score;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Lightweight HTTP server that serves the quiz UI and JSON API.
 *
 * Endpoints:
 *   GET  /               → serves web/index.html
 *   GET  /api/questions  → returns all questions as JSON
 *   POST /api/score      → saves a score  (body: {"name":"...","score":N})
 *   GET  /api/scores     → returns leaderboard as JSON
 */
public class QuizServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Serve the frontend HTML page
        server.createContext("/", QuizServer::handleFrontend);

        // API routes
        server.createContext("/api/questions", QuizServer::handleGetQuestions);
        server.createContext("/api/score",     QuizServer::handlePostScore);
        server.createContext("/api/scores",    QuizServer::handleGetScores);

        server.setExecutor(null); // default executor
        server.start();

        System.out.println("===========================================");
        System.out.println("  Quiz Server running at http://localhost:" + PORT);
        System.out.println("  Open your browser to start the quiz!");
        System.out.println("===========================================");
    }

    // ---------------------------------------------------------------
    //  Serve index.html
    // ---------------------------------------------------------------
    private static void handleFrontend(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405, "text/plain", "Method Not Allowed");
            return;
        }

        // Allow both running from IDE and from project root
        String[] candidates = {
            "web/index.html",
            "../web/index.html",
        };

        byte[] html = null;
        for (String path : candidates) {
            File f = new File(path);
            if (f.exists()) {
                html = Files.readAllBytes(f.toPath());
                break;
            }
        }

        if (html == null) {
            sendResponse(exchange, 404, "text/plain", "index.html not found. Make sure web/index.html exists.");
            return;
        }

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, html.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(html);
        }
    }

    // ---------------------------------------------------------------
    //  GET /api/questions
    // ---------------------------------------------------------------
    private static void handleGetQuestions(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        QuestionDAO dao = new QuestionDAO();
        List<Question> questions = dao.getAllQuestions();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            json.append("{")
                .append("\"id\":").append(q.getId()).append(",")
                .append("\"question\":\"").append(escapeJson(q.getQuestion())).append("\",")
                .append("\"answer\":\"").append(escapeJson(q.getAnswer())).append("\"")
                .append("}");
            if (i < questions.size() - 1) json.append(",");
        }
        json.append("]");

        sendResponse(exchange, 200, "application/json", json.toString());
    }

    // ---------------------------------------------------------------
    //  POST /api/score   body: {"name":"Ayush","score":8}
    // ---------------------------------------------------------------
    private static void handlePostScore(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, 405, "text/plain", "Method Not Allowed");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        // Simple JSON parse (no external lib needed)
        String name  = extractJsonString(body, "name");
        int    score = extractJsonInt(body, "score");

        if (name == null || name.isEmpty()) {
            sendResponse(exchange, 400, "application/json", "{\"error\":\"name is required\"}");
            return;
        }

        ScoreDAO dao = new ScoreDAO();
        dao.saveScore(new Score(name, score));

        sendResponse(exchange, 200, "application/json", "{\"status\":\"saved\"}");
    }

    // ---------------------------------------------------------------
    //  GET /api/scores
    // ---------------------------------------------------------------
    private static void handleGetScores(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        ScoreDAO dao = new ScoreDAO();
        List<Score> scores = dao.getAllScores();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < scores.size(); i++) {
            Score s = scores.get(i);
            json.append("{")
                .append("\"name\":\"").append(escapeJson(s.getUserName())).append("\",")
                .append("\"score\":").append(s.getScore()).append(",")
                .append("\"attemptedAt\":\"").append(s.getAttemptedAt() != null ? escapeJson(s.getAttemptedAt()) : "").append("\"")
                .append("}");
            if (i < scores.size() - 1) json.append(",");
        }
        json.append("]");

        sendResponse(exchange, 200, "application/json", json.toString());
    }

    // ---------------------------------------------------------------
    //  Helpers
    // ---------------------------------------------------------------
    private static void sendResponse(HttpExchange exchange, int code, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    /** Minimal JSON string extractor — finds "key":"value" */
    private static String extractJsonString(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return null;
        int colon = json.indexOf(":", idx + search.length());
        if (colon == -1) return null;
        int open = json.indexOf("\"", colon + 1);
        if (open == -1) return null;
        int close = json.indexOf("\"", open + 1);
        if (close == -1) return null;
        return json.substring(open + 1, close);
    }

    /** Minimal JSON int extractor — finds "key":N */
    private static int extractJsonInt(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return 0;
        int colon = json.indexOf(":", idx + search.length());
        if (colon == -1) return 0;
        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        try { return Integer.parseInt(json.substring(start, end)); }
        catch (NumberFormatException e) { return 0; }
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
