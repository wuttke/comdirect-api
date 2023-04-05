package eu.wuttke.comdirect.util;

import java.util.List;
import java.util.Map;

public class SimpleHttpResponse {

    private String body;
    private int statusCode;
    private Map<String, List<String>> headers;

    public SimpleHttpResponse(String body, int statusCode, Map<String, List<String>> headers) {
        this.body = body;
        this.statusCode = statusCode;
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
