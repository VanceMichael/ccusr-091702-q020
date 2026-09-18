package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class ApplicationTest {
    public static void main(String[] args) throws Exception {
        var server = Application.createServer("127.0.0.1", 0);
        server.start();
        try {
            var request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/health")).build();
            var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200 || !response.body().contains(Application.SERVICE)) throw new AssertionError(response.body());
        } finally { server.stop(0); }
    }
}