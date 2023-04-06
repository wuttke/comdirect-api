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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinalizeLoginServiceTest {

    @InjectMocks
    private FinalizeLoginService finalizeLoginService;

    @Mock
    private SimpleHttpClient httpClient;

    @Captor
    private ArgumentCaptor<String[]> headers1;

    @Test
    void testFinalizeLogin() throws ComdirectException, IOException {

        String patchBody = "{\"identifier\":\"sessionId\",\"sessionTanActive\":true,\"activated2FA\":true}";
        when(httpClient.patchForString(eq("https://api.comdirect.de/api/session/clients/user/v1/sessions/sessionId"),
                headers1.capture(), eq(patchBody)))
            .thenReturn(new SimpleHttpResponse("{\"identifier\":\"novelSessionId\"}", 200, null));

        String postBody = "client_id=clientId&client_secret=clientSecret&grant_type=cd_secondary&token=accessToken";
        when(httpClient.postForString("https://api.comdirect.de/oauth/token",
                new String[] {
                        "Accept", "application/json",
                        "Content-Type", "application/x-www-form-urlencoded"
                }, postBody))
            .thenReturn(new SimpleHttpResponse("{\"access_token\":\"novelAccessToken\",\"refresh_token\":\"novelRefreshToken\",\"expires_in\":599}", 200, null));

        var result = finalizeLoginService.finalizeLogin(new LoginCredentials("clientId", "clientSecret", "userName", "password"),
                new InitiateLoginResult(ChallengeType.PUSHTAN, "challengeId", "challenge",
                        new Tokens("accessToken", "refreshToken", null), "sessionId"),
                "myChallenge");

        assertEquals("novelSessionId", result.getSessionId());
        assertEquals("novelAccessToken", result.getTokens().getAccessToken());
        assertEquals("novelRefreshToken", result.getTokens().getRefreshToken());
        assertTrue(result.getTokens().getExpiry().after(new Date()));

        assertEquals("Bearer accessToken", headers1.getValue()[3]);
        assertEquals("x-once-authentication-info", headers1.getValue()[8]);
        assertEquals("{\"id\":\"challengeId\"}", headers1.getValue()[9]);
        assertEquals("x-once-authentication", headers1.getValue()[10]);
        assertEquals("myChallenge", headers1.getValue()[11]);
    }

}