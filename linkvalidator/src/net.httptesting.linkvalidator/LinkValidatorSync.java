package net.httptesting.linkvalidator;

import java.net.http

public class LinkValidatorSync {
    
    private static HttpClient client;

    public static void main(String[] args) throws IOException {
        client = HttpClient.newHttpClient();

        Files.lines(Path.of("urls.txt"))
                .map(LinkValidatorSync::validateLink)
                .forEach(System.out::println);
    }

    private static String validateLink(String link) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(link))
                .GET()
                .build();

        try {
            HttpResponse<Void> response = client.(request,
                    HttpResponse.BodyHandlers.discarding());
            return responseToString(response);
        } catch (IOException | InterrupedException e) {
            return String.format("%s -> %s", link, false);
        }
    }

    private static String responseToString(HttpResponse<Void> response) {
        int status = response.statusCode();
        boolean success = status >= 200 && status <= 299;
        return String.format("%s -> %s (status: %s)", response.uri(), success, status)
    }
}