package eu.wuttke.comdirect.login;

import eu.wuttke.comdirect.account.AccountService;
import eu.wuttke.comdirect.account.AccountsPage;
import eu.wuttke.comdirect.account.TransactionsPage;
import eu.wuttke.comdirect.util.ComdirectException;
import eu.wuttke.comdirect.util.SimpleHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceIT {

    @Test
    void testLogin() throws ComdirectException {
        SimpleHttpClient myClient = new SimpleHttpClient();

        LoginCredentials credentials = new LoginCredentials(
                System.getenv("clientId"),
                System.getenv("clientSecret"),
                System.getenv("userName"),
                System.getenv("password")
        );

        InitiateLoginService service1 = new InitiateLoginService(myClient);
        InitiateLoginResult result1 = service1.initiateLogin(credentials);
        assertEquals(ChallengeType.PUSHTAN, result1.getChallengeType());

        FinalizeLoginService service2 = new FinalizeLoginService(myClient);
        ComdirectSession session = service2.finalizeLogin(credentials, result1, "");
        assertNotNull(session.getTokens().getAccessToken());

        AccountService service3 = new AccountService(myClient);
        AccountsPage accounts = service3.getAccounts(session);
        assertTrue(accounts.getValues().size() > 0);

        TransactionsPage transactions = service3.getTransactions(session, accounts.getValues().get(0).getAccountId());
        assertTrue(transactions.getValues().size() > 0);
    }

}