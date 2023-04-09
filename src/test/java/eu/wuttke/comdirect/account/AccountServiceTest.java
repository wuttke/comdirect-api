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
        assertEquals("Authorization", headers.getValue()[2]);
        assertEquals("Bearer " + session.getTokens().getAccessToken(), headers.getValue()[3]);
        assertEquals("x-http-request-info", headers.getValue()[4]);

        AccountBalance balance = page.getValues().get(0);
        Account account = balance.getAccount();
        assertEquals("64F12122866E43D58984BD2CE60FB84D", balance.getAccountId());
        assertEquals(123.45, balance.getBalance().getValue());
        assertEquals(123.45, balance.getBalance().getValue());
        assertEquals(123.45, balance.getBalanceEUR().getValue());
        assertEquals(123.45, balance.getAvailableCashAmount().getValue());
        assertEquals(123.45, balance.getAvailableCashAmountEUR().getValue());

        assertEquals("64F12122866E43D58984BD2CE60FB84D", account.getAccountId());
        assertEquals("102327881900", account.getAccountDisplayId());
        assertEquals("EUR", account.getCurrency());
        assertEquals("5CBCEE21441E4669B6E0C162C95737BE", account.getClientId());
        assertEquals("CA", account.getAccountType().getKey());
        assertEquals("DE14200411330327881900", account.getIban());
        assertEquals("COBADEHD001", account.getBic());
        assertEquals(123, account.getCreditLimit().getValue());
    }

    @Test
    void getTransactions() throws ComdirectException, IOException {
        when(httpClient.getForString(
                eq("https://api.comdirect.de/api/banking/v1/accounts/Account123/transactions?transactionDirection=CREDIT_AND_DEBIT&transactionState=BOTH&paging-first=0&paging-count=100"),
                headers.capture()))
                .thenReturn(new SimpleHttpResponse(transactionsTestData, 200, new HashMap<>()));
        var page = service.getTransactions(session, "Account123");
        assertEquals(1, page.getValues().size());

        assertEquals("Accept", headers.getValue()[0]);
        assertEquals("application/json", headers.getValue()[1]);
        assertEquals("Authorization", headers.getValue()[2]);
        assertEquals("Bearer " + session.getTokens().getAccessToken(), headers.getValue()[3]);
        assertEquals("x-http-request-info", headers.getValue()[4]);

        Transaction t1 = page.getValues().get(0);
        assertEquals("6V2C1LVP2LJQ958S/22169", t1.getReference());

        assertEquals("BOOKED", t1.getBookingStatus());
        assertEquals("2023-04-05", t1.getBookingDate());
        assertEquals(-22.0, t1.getAmount().getValue());
        assertEquals("EUR", t1.getAmount().getUnit());
        assertEquals("GASTST PARADIES", t1.getRemitter().getHolderName());
        assertEquals(null, t1.getDeptor());
        assertEquals(null, t1.getCreditor());
        assertEquals("2023-04-05", t1.getValutaDate());
        assertEquals(null, t1.getDirectDebitCreditorId());
        assertEquals(null, t1.getDirectDebitMandateId());
        assertEquals(null, t1.getEndToEndReference());
        assertEquals(true, t1.isNewTransaction());
        assertEquals("01GASTSTAETTE PARADIES//FREIBURG/DE  022023-04-04T13:46:59 KFN 1  VJ 2312 ", t1.getRemittanceInfo());
        assertEquals("Direct Debit", t1.getTransactionType().getText());
        assertEquals("DIRECT_DEBIT", t1.getTransactionType().getKey());
    }

    private static String accountsTestData = "{\"paging\":{\"index\":0,\"matches\":3},\"values\":[{\"account\":{\"accountId\":\"64F12122866E43D58984BD2CE60FB84D\",\"accountDisplayId\":\"102327881900\",\"currency\":\"EUR\",\"clientId\":\"5CBCEE21441E4669B6E0C162C95737BE\",\"accountType\":{\"key\":\"CA\",\"text\":\"Girokonto\"},\"iban\":\"DE14200411330327881900\",\"bic\":\"COBADEHD001\",\"creditLimit\":{\"value\":123.0,\"unit\":\"EUR\"}},\"accountId\":\"64F12122866E43D58984BD2CE60FB84D\",\"balance\":{\"value\":123.45,\"unit\":\"EUR\"},\"balanceEUR\":{\"value\":123.45,\"unit\":\"EUR\"},\"availableCashAmount\":{\"value\":123.45,\"unit\":\"EUR\"},\"availableCashAmountEUR\":{\"value\":123.45,\"unit\":\"EUR\"}},{\"account\":{\"accountId\":\"B04B6144107E4D59A431C506A684F6EA\",\"accountDisplayId\":\"102327881905\",\"currency\":\"EUR\",\"clientId\":\"5CBCEE21441E4669B6E0C162C95737BE\",\"accountType\":{\"key\":\"DAS\",\"text\":\"Tagesgeld PLUS-Konto\"},\"iban\":\"DE73200411330327881905\",\"bic\":\"COBADEHD001\",\"creditLimit\":{\"value\":0.0,\"unit\":\"EUR\"}},\"accountId\":\"B04B6144107E4D59A431C506A684F6EA\",\"balance\":{\"value\":0.0,\"unit\":\"EUR\"},\"balanceEUR\":{\"value\":0.0,\"unit\":\"EUR\"},\"availableCashAmount\":{\"value\":0.0,\"unit\":\"EUR\"},\"availableCashAmountEUR\":{\"value\":0.0,\"unit\":\"EUR\"}},{\"account\":{\"accountId\":\"EB212CD8BC20467B8C9F4764D8B577C6\",\"accountDisplayId\":\"102327881980\",\"currency\":\"USD\",\"clientId\":\"5CBCEE21441E4669B6E0C162C95737BE\",\"accountType\":{\"key\":\"FX\",\"text\":\"Fremdwährungskonto\"},\"iban\":\"DE85200411330327881980\",\"bic\":\"COBADEHD001\",\"creditLimit\":{\"value\":0.0,\"unit\":\"EUR\"}},\"accountId\":\"EB212CD8BC20467B8C9F4764D8B577C6\",\"balance\":{\"value\":0.0,\"unit\":\"USD\"},\"balanceEUR\":{\"value\":0.0,\"unit\":\"EUR\"},\"availableCashAmount\":{\"value\":0.0,\"unit\":\"USD\"},\"availableCashAmountEUR\":{\"value\":0.0,\"unit\":\"EUR\"}}]}\n";
    private static String transactionsTestData = "{\"paging\":{\"index\":0,\"matches\":423},\"aggregated\":{\"accountId\":\"64F12122866E43D58984BD2CE60FB84D\",\"bookingDateLatestTransaction\":\"2023-02-22\",\"referenceLatestTransaction\":\"6871578189\",\"latestTransactionIncluded\":false,\"pagingTimestamp\":1680709409000},\"values\":[{\"reference\":\"6V2C1LVP2LJQ958S/22169\",\"bookingStatus\":\"BOOKED\",\"bookingDate\":\"2023-04-05\",\"amount\":{\"value\":-22.0,\"unit\":\"EUR\"},\"remitter\":{\"holderName\":\"GASTST PARADIES\",\"iban\":null,\"bic\":null},\"deptor\":null,\"creditor\":null,\"valutaDate\":\"2023-04-05\",\"directDebitCreditorId\":null,\"directDebitMandateId\":null,\"endToEndReference\":null,\"newTransaction\":true,\"remittanceInfo\":\"01GASTSTAETTE PARADIES//FREIBURG/DE  022023-04-04T13:46:59 KFN 1  VJ 2312 \",\"transactionType\":{\"key\":\"DIRECT_DEBIT\",\"text\":\"Direct Debit\"}}]}\n";

}