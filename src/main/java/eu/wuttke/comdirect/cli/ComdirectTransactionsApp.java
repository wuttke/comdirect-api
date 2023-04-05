package eu.wuttke.comdirect.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.wuttke.comdirect.account.AccountService;
import eu.wuttke.comdirect.account.AccountsPage;
import eu.wuttke.comdirect.account.TransactionsPage;
import eu.wuttke.comdirect.login.*;
import eu.wuttke.comdirect.util.ComdirectException;
import eu.wuttke.comdirect.util.SimpleHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ComdirectTransactionsApp {

    public static void main(String[] args) throws IOException, ComdirectException {
        SimpleHttpClient myClient = new SimpleHttpClient();

        LoginCredentials credentials = new LoginCredentials(
                System.getenv("clientId"),
                System.getenv("clientSecret"),
                System.getenv("userName"),
                System.getenv("password")
        );

        InitiateLoginService service1 = new InitiateLoginService(myClient);
        InitiateLoginResult result1 = service1.initiateLogin(credentials);

        System.out.println("Press ENTER after accepting the Push TAN");
        new BufferedReader(new InputStreamReader(System.in)).readLine();

        FinalizeLoginService service2 = new FinalizeLoginService(myClient);
        ComdirectSession session = service2.finalizeLogin(credentials, result1, "");

        AccountService service3 = new AccountService(myClient);
        AccountsPage accounts = service3.getAccounts(session);
        new ObjectMapper().writerFor(AccountsPage.class).writeValue(System.out, accounts);

        TransactionsPage transactions = service3.getTransactions(session, accounts.getValues().get(0).getAccountId());
        new ObjectMapper().writerFor(TransactionsPage.class).writeValue(System.out, transactions);
    }

}