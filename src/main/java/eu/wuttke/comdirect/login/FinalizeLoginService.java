package eu.wuttke.comdirect.login;

import eu.wuttke.comdirect.util.BaseComdirectService;
import eu.wuttke.comdirect.util.ComdirectException;
import eu.wuttke.comdirect.util.SimpleHttpClient;
import eu.wuttke.comdirect.util.SimpleHttpResponse;

import java.io.IOException;
import java.util.Map;

public class FinalizeLoginService extends BaseComdirectService {

    public FinalizeLoginService(SimpleHttpClient httpClient) {
        super(httpClient);
    }

    public ComdirectSession finalizeLogin(
            LoginCredentials credentials,
            InitiateLoginResult initiateLoginResult,
            String challengeResponse) throws ComdirectException {
        String sessionId = patchSessionWithChallenge(initiateLoginResult, challengeResponse);
        Map<String, Object> data = postCustomOAuthForMap(credentials, initiateLoginResult);

        return new ComdirectSession(
                new Tokens((String)data.get(ACCESS_TOKEN),
                    (String)data.get(REFRESH_TOKEN),
                    calculateExpiry((Integer)data.get(EXPIRES_IN))),
                sessionId);
    }

    private Map<String, Object> postCustomOAuthForMap(LoginCredentials credentials, InitiateLoginResult initiateLoginResult) throws ComdirectException {
        try {
            String body = "client_id=" + credentials.getClientId() +
                    "&client_secret=" + credentials.getClientSecret() +
                    "&grant_type=cd_secondary" +
                    "&token=" + initiateLoginResult.getTokens().getAccessToken();
            SimpleHttpResponse response = httpClient.postForString(comdirectApiEndpoint + "/oauth/token",
                    new String[] {
                        ACCEPT_HEADER, APPLICATION_JSON,
                        CONTENT_TYPE_HEADER, APPLICATION_X_WWW_FORM_URLENCODED
                    },
                    body);
            if (response.getStatusCode() != 200)
                throw new ComdirectException("unable to post for secondary token", response.getStatusCode(), response.getBody());

            return objectMapper.readerFor(Map.class).readValue(response.getBody());
        } catch (IOException e) {
            throw new ComdirectException("unable to post for secondary token", 0,
                    e.getMessage());
        }
    }

    private String patchSessionWithChallenge(InitiateLoginResult initiateLoginResult, String challengeResponse) throws ComdirectException {
        try {
            String body = "{" +
                        "\"identifier\":\"" + initiateLoginResult.getSessionId() + "\"," +
                        "\"sessionTanActive\":true," +
                        "\"activated2FA\":true" +
                    "}";

            SimpleHttpResponse response = httpClient.patchForString(
                    comdirectApiEndpoint + "/api/session/clients/user/v1/sessions/" + initiateLoginResult.getSessionId(),
                    new String[] {
                        ACCEPT_HEADER, APPLICATION_JSON,
                        AUTHORIZATION_HEADER, BEARER + initiateLoginResult.getTokens().getAccessToken(),
                        X_HTTP_REQUEST_INFO_HEADER, buildRequestInfoHeader(initiateLoginResult.getSessionId()),
                        CONTENT_TYPE_HEADER, APPLICATION_JSON,
                        "x-once-authentication-info", "{\"id\":\"" + initiateLoginResult.getChallengeId() + "\"}",
                        "x-once-authentication", challengeResponse
                    },
                    body);

            if (response.getStatusCode() != 200)
                throw new ComdirectException("unable to patch session with challenge response", response.getStatusCode(), response.getBody());

            Map<String, String> session = objectMapper.readerFor(Map.class).readValue(response.getBody());
            return session.get("identifier");
        } catch (IOException e) {
            throw new ComdirectException("unable to patch session with challenge response", 0,
                    e.getMessage());
        }
    }

}
