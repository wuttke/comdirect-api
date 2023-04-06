package eu.wuttke.comdirect.login;

import eu.wuttke.comdirect.util.ComdirectException;
import eu.wuttke.comdirect.util.SimpleHttpClient;
import eu.wuttke.comdirect.util.SimpleHttpResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
class InitiateLoginServiceTest {

    @InjectMocks
    private InitiateLoginService initiateLoginService;

    @Mock
    private SimpleHttpClient httpClient;

    @Captor
    private ArgumentCaptor<String[]> headers1;

    @Captor
    private ArgumentCaptor<String[]> headers2;

    @Test
    void initiateLogin() throws ComdirectException, IOException {
        when(httpClient.postForString("https://api.comdirect.de/oauth/token",
                new String[] {
                        "Accept", "application/json",
                        "Content-Type", "application/x-www-form-urlencoded"
                },
                "client_id=clientId&client_secret=clientSecret&username=userName&password=password&grant_type=password"
        )).thenReturn(new SimpleHttpResponse(TOKEN_RESPONSE, 200, null));

        when(httpClient.getForString(eq("https://api.comdirect.de/api/session/clients/user/v1/sessions"),
             headers1.capture()
        )).thenReturn(new SimpleHttpResponse(SESSION_RESPONSE, 200, null));

        Map<String, List<String>> responseHeaders = new HashMap<>();
        responseHeaders.put("x-once-authentication-info", List.of("{\"id\":\"266504001\",\"typ\":\"P_TAN_PUSH\",\"availableTypes\":[\"P_TAN_PUSH\",\"P_TAN\",\"P_TAN_APP\"],\"link\":{\"href\":\"/api/session/v1/authentications/B2028C37A3384922897A362E049F06C3\",\"rel\":\"related\",\"method\":\"GET\",\"type\":\"application/json\"}}"));
        when(httpClient.postForString(eq("https://api.comdirect.de/api/session/clients/user/v1/sessions/901CDD426AF14D8685C8F3DA456C3C65/validate"),
                headers2.capture(),
                eq(POST_TOKEN_BODY)
        )).thenReturn(new SimpleHttpResponse(POST_TOKEN_RESPONSE, 201, responseHeaders));

        var result = initiateLoginService.initiateLogin(new LoginCredentials("clientId", "clientSecret", "userName", "password"));

        assertEquals("266504001", result.getChallengeId());
        assertEquals("901CDD426AF14D8685C8F3DA456C3C65", result.getSessionId());
        assertEquals(ChallengeType.PUSHTAN, result.getChallengeType());
        assertEquals("abc", result.getTokens().getAccessToken());
        assertEquals("def", result.getTokens().getRefreshToken());
        assertNull(result.getChallenge());
        assertTrue(result.getTokens().getExpiry().after(new Date()));
    }

    private static String TOKEN_RESPONSE = "{\"access_token\":\"abc\",\"token_type\":\"bearer\",\"refresh_token\":\"def\",\"expires_in\":599,\"scope\":\"TWO_FACTOR\",\"kdnr\":\"xx\",\"bpid\":123,\"kontaktId\":456}";
    private static String SESSION_RESPONSE = "[{\"identifier\":\"901CDD426AF14D8685C8F3DA456C3C65\",\"sessionTanActive\":false,\"activated2FA\":false}]";
    private static String POST_TOKEN_BODY = "{\"identifier\":\"901CDD426AF14D8685C8F3DA456C3C65\",\"sessionTanActive\":true,\"activated2FA\":true}";
    private static String POST_TOKEN_RESPONSE = "{\"identifier\":\"901CDD426AF14D8685C8F3DA456C3C65\",\"sessionTanActive\":true,\"activated2FA\":true}";

}