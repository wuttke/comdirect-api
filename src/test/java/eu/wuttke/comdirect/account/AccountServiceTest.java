package eu.wuttke.comdirect.account;

import eu.wuttke.comdirect.login.ComdirectSession;
import eu.wuttke.comdirect.login.Tokens;
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
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService service;

    @Mock
    private SimpleHttpClient httpClient;

    @Captor
    private ArgumentCaptor<String[]> headers;

    private ComdirectSession session = new ComdirectSession(
            new Tokens("accessToken", "refreshToken", new Date()),
            "sessionId"
    );

    @Test
    void getAccounts() throws ComdirectException, IOException {
        when(httpClient.getForString(
                eq("https://api.comdirect.de/api/banking/clients/user/v1/accounts/balances?paging-first=0&paging-count=1000"),
                headers.capture()))
            .thenReturn(new SimpleHttpResponse(accountsTestData, 200, new HashMap<>()));
        var page = service.getAccounts(session);
        assertEquals(3, page.getValues().size());
        assertEquals("Accept", headers.getValue()[0]);
        assertEquals("application/json", headers.getValue()[1]);
        assertEquals("Authorisation", headers.getValue()[2]);
        assertEquals("Bearer " + session.getTokens().getAccessToken(), headers.getValue()[3]);
        assertEquals("x-http-request-info", headers.getValue()[4]);
    }

    private static String accountsTestData = "{\"paging\":{\"index\":0,\"matches\":3},\"values\":[{\"account\":{\"accountId\":\"64F12122866E43D58984BD2CE60FB84D\",\"accountDisplayId\":\"102327881900\",\"currency\":\"EUR\",\"clientId\":\"5CBCEE21441E4669B6E0C162C95737BE\",\"accountType\":{\"key\":\"CA\",\"text\":\"Girokonto\"},\"iban\":\"DE14200411330327881900\",\"bic\":\"COBADEHD001\",\"creditLimit\":{\"value\":123.0,\"unit\":\"EUR\"}},\"accountId\":\"64F12122866E43D58984BD2CE60FB84D\",\"balance\":{\"value\":123.45,\"unit\":\"EUR\"},\"balanceEUR\":{\"value\":123.45,\"unit\":\"EUR\"},\"availableCashAmount\":{\"value\":123.45,\"unit\":\"EUR\"},\"availableCashAmountEUR\":{\"value\":123.45,\"unit\":\"EUR\"}},{\"account\":{\"accountId\":\"B04B6144107E4D59A431C506A684F6EA\",\"accountDisplayId\":\"102327881905\",\"currency\":\"EUR\",\"clientId\":\"5CBCEE21441E4669B6E0C162C95737BE\",\"accountType\":{\"key\":\"DAS\",\"text\":\"Tagesgeld PLUS-Konto\"},\"iban\":\"DE73200411330327881905\",\"bic\":\"COBADEHD001\",\"creditLimit\":{\"value\":0.0,\"unit\":\"EUR\"}},\"accountId\":\"B04B6144107E4D59A431C506A684F6EA\",\"balance\":{\"value\":0.0,\"unit\":\"EUR\"},\"balanceEUR\":{\"value\":0.0,\"unit\":\"EUR\"},\"availableCashAmount\":{\"value\":0.0,\"unit\":\"EUR\"},\"availableCashAmountEUR\":{\"value\":0.0,\"unit\":\"EUR\"}},{\"account\":{\"accountId\":\"EB212CD8BC20467B8C9F4764D8B577C6\",\"accountDisplayId\":\"102327881980\",\"currency\":\"USD\",\"clientId\":\"5CBCEE21441E4669B6E0C162C95737BE\",\"accountType\":{\"key\":\"FX\",\"text\":\"Fremdwährungskonto\"},\"iban\":\"DE85200411330327881980\",\"bic\":\"COBADEHD001\",\"creditLimit\":{\"value\":0.0,\"unit\":\"EUR\"}},\"accountId\":\"EB212CD8BC20467B8C9F4764D8B577C6\",\"balance\":{\"value\":0.0,\"unit\":\"USD\"},\"balanceEUR\":{\"value\":0.0,\"unit\":\"EUR\"},\"availableCashAmount\":{\"value\":0.0,\"unit\":\"USD\"},\"availableCashAmountEUR\":{\"value\":0.0,\"unit\":\"EUR\"}}]}\n";

    /*@Test
    void getTransactions() {
    }*/
}