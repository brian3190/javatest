package net.httptesting.linkvalidator;

import java.net.http

public class LinkValidatorBuilder {
    
    private static HttpClient client;

    public static void main(String[] args) throws IOException {
        client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(3))
                    // Redirects connection to a website with full url
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

        var futures = Files.lines(Path.of("urls.txt"))
                .map(LinkValidatorASync::validateLink)
                .collect(Collectors.toList());

        futures.stream()
                .map(CompletableFuture::join)
                .forEach(System.out::println);
    }

    private static CompletableFuture<String> validateLink(String link) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(link))
                // Connection timeout after 2 secs
                .timeout(Duration.ofSeconds(2))
                .GET()
                .build();

        // Asynchronously send httprequest;
        return client.sendAsync(request, HttpResponse.BodyHandlers.discarding());
                     .thenApply(LinkValidatorASync::responseToString)
                     .exceptionally(e -> String.format("%s -> %s", link, false))
    }

    private static String responseToString(HttpResponse<Void> response) {
        int status = response.statusCode();
        boolean success = status >= 200 && status <= 299;
        return String.format("%s -> %s (status: %s)", response.uri(), success, status)
    }
}