package eu.wuttke.comdirect.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.wuttke.comdirect.util.SimpleHttpClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseLoginService {

    protected String comdirectApiEndpoint = "https://api.comdirect.de";
    protected SimpleHttpClient httpClient = new SimpleHttpClient();
    protected ObjectMapper objectMapper = new ObjectMapper();

    protected String buildRequestInfoHeader(String sessionId) {
        String requestId = generateTimestamp();
        return "{\"clientRequestId\":{" +
                "\"sessionId\":\"" + sessionId + "\"," +
                "\"requestId\":\"" + requestId + "\"}}";
    }

    protected Date calculateExpiry(Integer expiresIn) {
        if (expiresIn == null)
            return null;
        Date now = new Date();
        return new Date(now.getTime() + expiresIn * 1000L);
    }

    private String generateTimestamp() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return simpleDateFormat.format(now);
    }

    protected static final String ACCEPT_HEADER = "Accept";
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String CONTENT_TYPE_HEADER = "Content-Type";
    protected static final String APPLICATION_JSON = "application/json";
    protected static final String BEARER = "Bearer ";
    protected static final String ACCESS_TOKEN = "access_token";
    protected static final String REFRESH_TOKEN = "refresh_token";
    protected static final String EXPIRES_IN = "expires_in";
    protected static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    protected static final String X_HTTP_REQUEST_INFO_HEADER = "x-http-request-info";


}
