package eu.wuttke.comdirect.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.wuttke.comdirect.util.ComdirectException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

public class InitiateLoginService extends BaseLoginService {

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

            HttpResponse<String> response = httpClient.postForString(
                    comdirectApiEndpoint + "/api/session/clients/user/v1/sessions/" + sessionId + "/validate",
                    new String[] {
                        ACCEPT_HEADER, APPLICATION_JSON,
                        AUTHORIZATION_HEADER, BEARER + tokens.getAccessToken(),
                        X_HTTP_REQUEST_INFO_HEADER, buildRequestInfoHeader(sessionId),
                        CONTENT_TYPE_HEADER, APPLICATION_JSON
                    },
                    body);

            if (response.statusCode() != 201)
                throw new ComdirectException("unable to validate session", response.statusCode(), response.body());

            String authInfoStr = response.headers().firstValue("x-once-authentication-info").orElseThrow();
            return objectMapper.readerFor(Map.class).readValue(authInfoStr);
        } catch (IOException e) {
            throw new ComdirectException("unable to validate session", 0,
                    e.getMessage());
        }
    }

    private String generateAndGetSessionId(Tokens tokens) throws ComdirectException {
        try {
            String sessionId = UUID.randomUUID().toString();

            HttpResponse<String> response = httpClient.getForString(comdirectApiEndpoint + "/api/session/clients/user/v1/sessions",
                    new String[]{
                            ACCEPT_HEADER, APPLICATION_JSON,
                            AUTHORIZATION_HEADER, BEARER + tokens.getAccessToken(),
                            X_HTTP_REQUEST_INFO_HEADER, buildRequestInfoHeader(sessionId)
                    });

            if (response.statusCode() != 200)
                throw new ComdirectException("unable to get session", response.statusCode(), response.body());

            Map[] sessions = objectMapper.readerFor(Map[].class).readValue(response.body());
            return (String) sessions[0].get("identifier");
        } catch (IOException e) {
            throw new ComdirectException("unable to get session", 0,
                    e.getMessage());
        }
    }

    private Tokens postCredentialsForTokens(LoginCredentials credentials) throws ComdirectException {
        try {
            HttpResponse<String> response = httpClient.postForString(
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

            if (response.statusCode() != 200)
                throw new ComdirectException("unable to obtain access token", response.statusCode(), response.body());

            return parseTokens(response.body());
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
