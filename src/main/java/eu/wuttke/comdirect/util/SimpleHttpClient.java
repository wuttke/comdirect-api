package eu.wuttke.comdirect.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class SimpleHttpClient {

    public HttpResponse<String> postForString(String url, String[] headers, String bodyStr)
            throws IOException {
        return performRequestForString("POST", url, headers, bodyStr);
    }

    public HttpResponse<String> getForString(String url, String[] headers)
            throws IOException {
        return performRequestForString("GET", url, headers, "");
    }

    public HttpResponse<String> patchForString(String url, String[] headers, String bodyStr)
            throws IOException {
        return performRequestForString("PATCH", url, headers, bodyStr);
    }

    private HttpResponse<String> performRequestForString(
            String method, String url, String[] headers, String bodyStr)
    throws IOException {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .headers(headers)
                    .method(method, HttpRequest.BodyPublishers.ofString(bodyStr))
                    .timeout(Duration.of(10, SECONDS))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | URISyntaxException e) {
            throw new IOException("unable to perform HTTP request", e);
        }
    }

}
