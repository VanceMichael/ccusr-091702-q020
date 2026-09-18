package org.example;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public final class Application {
    public static final String SERVICE = "reading_review";
    private Application() {}
    public static HttpServer createServer(String host, int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/health", exchange -> {
            if (!"GET".equals(exchange.getRequestMethod())) { exchange.sendResponseHeaders(405, -1); return; }
            byte[] body = ("{\"status\":\"ok\",\"service\":\"" + SERVICE + "\"}").getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, body.length);
            try (var output = exchange.getResponseBody()) { output.write(body); }
        });
        return server;
    }
    public static void main(String[] args) throws Exception {
        String host = System.getenv().getOrDefault("HOST", "0.0.0.0");
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        createServer(host, port).start();
    }
}