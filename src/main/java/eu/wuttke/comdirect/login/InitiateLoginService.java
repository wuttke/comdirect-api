package eu.wuttke.comdirect.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.wuttke.comdirect.util.BaseComdirectService;
import eu.wuttke.comdirect.util.ComdirectException;
import eu.wuttke.comdirect.util.SimpleHttpClient;
import eu.wuttke.comdirect.util.SimpleHttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class InitiateLoginService extends BaseComdirectService {

    private static final String X_ONCE_AUTHENTICATION_INFO = "x-once-authentication-info";

    public InitiateLoginService(SimpleHttpClient httpClient) {
        super(httpClient);
    }

    public InitiateLoginResult initiateLogin(LoginCredentials credentials) throws ComdirectException {
        Tokens tokens = postCredentialsForTokens(credentials);
        String sessionId = generateAndGetSessionId(tokens);
        Map<String,String> authInfo = postToValidateSession(tokens, sessionId);

        return new InitiateLoginResult(
                ChallengeType.decodeChallengeType(authInfo.get("typ")),
                authInfo.get("id"),
                authInfo.get("challenge"),
                tokens, sessionId);
    }

    private Map<String, String> postToValidateSession(Tokens tokens, String sessionId) throws ComdirectException {
        try {
            String body = "{\"identifier\":\"" + sessionId + "\"," +
                "\"sessionTanActive\":true," +
                "\"activated2FA\":true}";

            SimpleHttpResponse response = httpClient.postForString(
                    comdirectApiEndpoint + "/api/session/clients/user/v1/sessions/" + sessionId + "/validate",
                    new String[] {
                        ACCEPT_HEADER, APPLICATION_JSON,
                        AUTHORIZATION_HEADER, BEARER + tokens.getAccessToken(),
                        X_HTTP_REQUEST_INFO_HEADER, buildRequestInfoHeader(sessionId),
                        CONTENT_TYPE_HEADER, APPLICATION_JSON
                    },
                    body);

            if (response.getStatusCode() != 201)
                throw new ComdirectException("unable to validate session", response.getStatusCode(), response.getBody());
            if (!response.getHeaders().containsKey(X_ONCE_AUTHENTICATION_INFO))
                throw new ComdirectException("unable to validate session: missing header \"x-once-authentication-info\"", -1, response.getBody());

            String authInfoStr = response.getHeaders().get(X_ONCE_AUTHENTICATION_INFO).get(0);
            return objectMapper.readerFor(Map.class).readValue(authInfoStr);
        } catch (IOException e) {
            throw new ComdirectException("unable to validate session", 0,
                    e.getMessage());
        }
    }

    private String generateAndGetSessionId(Tokens tokens) throws ComdirectException {
        try {
            String sessionId = UUID.randomUUID().toString();

            SimpleHttpResponse response = httpClient.getForString(comdirectApiEndpoint + "/api/session/clients/user/v1/sessions",
                    new String[]{
                            ACCEPT_HEADER, APPLICATION_JSON,
                            AUTHORIZATION_HEADER, BEARER + tokens.getAccessToken(),
                            X_HTTP_REQUEST_INFO_HEADER, buildRequestInfoHeader(sessionId)
                    });

            if (response.getStatusCode() != 200)
                throw new ComdirectException("unable to get session", response.getStatusCode(), response.getBody());

            Map[] sessions = objectMapper.readerFor(Map[].class).readValue(response.getBody());
            return (String) sessions[0].get("identifier");
        } catch (IOException e) {
            throw new ComdirectException("unable to get session", 0,
                    e.getMessage());
        }
    }

    private Tokens postCredentialsForTokens(LoginCredentials credentials) throws ComdirectException {
        try {
            SimpleHttpResponse response = httpClient.postForString(
                    comdirectApiEndpoint + "/oauth/token",
                    new String[]{
                            ACCEPT_HEADER, APPLICATION_JSON,
                            CONTENT_TYPE_HEADER, APPLICATION_X_WWW_FORM_URLENCODED
                    },
                    "client_id=" + credentials.getClientId() +
                            "&client_secret=" + credentials.getClientSecret() +
                            "&username=" + credentials.getUserName() +
                            "&password=" + credentials.getPassword() +
                            "&grant_type=password");

            if (response.getStatusCode() != 200)
                throw new ComdirectException("unable to obtain access token", response.getStatusCode(), response.getBody());

            return parseTokens(response.getBody());
        } catch (IOException e) {
            throw new ComdirectException("unable to obtain access token", 0,
                    e.getMessage());
        }
    }

    private Tokens parseTokens(String body) throws JsonProcessingException {
        Map<String,Object> data = objectMapper.readerFor(Map.class).readValue(body);
        return new Tokens((String)data.get(ACCESS_TOKEN),
                (String)data.get(REFRESH_TOKEN),
                calculateExpiry((Integer)data.get(EXPIRES_IN)));
    }

}
