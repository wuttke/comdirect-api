package eu.wuttke.comdirect.login;

import eu.wuttke.comdirect.account.AccountService;
import eu.wuttke.comdirect.account.AccountsPage;
import eu.wuttke.comdirect.util.ComdirectException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceIT {

    @Test
    void testLogin() throws ComdirectException {
        LoginCredentials credentials = new LoginCredentials(
                System.getenv("clientId"),
                System.getenv("clientSecret"),
                System.getenv("userName"),
                System.getenv("password")
        );

        InitiateLoginService service1 = new InitiateLoginService();
        InitiateLoginResult result1 = service1.initiateLogin(credentials);
        assertEquals(ChallengeType.PUSHTAN, result1.getChallengeType());

        FinalizeLoginService service2 = new FinalizeLoginService();
        ComdirectSession session = service2.finalizeLogin(credentials, result1, "");
        assertNotNull(session.getTokens().getAccessToken());

        AccountService service3 = new AccountService();
        AccountsPage accounts = service3.getAccounts(session);

        service3.getTransactions(session, accounts.getValues().get(0).getAccountId());

    }

}