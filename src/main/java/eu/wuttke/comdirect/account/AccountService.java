package eu.wuttke.comdirect.account;

import eu.wuttke.comdirect.login.ComdirectSession;
import eu.wuttke.comdirect.util.BaseComdirectService;
import eu.wuttke.comdirect.util.ComdirectException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class AccountService extends BaseComdirectService {

    public AccountsPage getAccounts(ComdirectSession session) throws ComdirectException {
        try {
            HttpResponse<String> response = getWithTokens(session,
                    comdirectApiEndpoint +
                            "/api/banking/clients/user/v1/accounts/balances" +
                            "?paging-first=0&paging-count=1000");
            if (response.statusCode() != 200)
                throw new ComdirectException("error listing accounts", response.statusCode(), response.body());
            return objectMapper.readerFor(AccountsPage.class).readValue(response.body());
        } catch (IOException e) {
            throw new ComdirectException("unable to list accounts", 0, e.getMessage());
        }
    }

    public List<Object> getTransactions(ComdirectSession session, String accountId) throws ComdirectException {
        try {
            HttpResponse<String> response = getWithTokens(session,
                    comdirectApiEndpoint +
                            "/api/banking/v1/accounts/" + accountId +
                            "/transactions?transactionDirection=CREDIT_AND_DEBIT&transactionState=BOTH");
            if (response.statusCode() != 200)
                throw new ComdirectException("error listing transactions", response.statusCode(), response.body());
            // TODO parse accounts
            return null;
        } catch (IOException e) {
            throw new ComdirectException("unable to list accounts", 0, e.getMessage());
        }
    }

    private HttpResponse<String> getWithTokens(ComdirectSession session, String url) throws IOException {
        return httpClient.getForString(url, new String[] {
                ACCEPT_HEADER, APPLICATION_JSON,
                AUTHORIZATION_HEADER, BEARER + session.getTokens().getAccessToken(),
                X_HTTP_REQUEST_INFO_HEADER, buildRequestInfoHeader(session.getSessionId())
        });
    }

}
