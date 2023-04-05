# Comdirect REST API

![Maven Build Badge](https://github.com/wuttke/comdirect-api/actions/workflows/maven.yml/badge.svg)
![JaCoCo Unit-test Coverage](https://wuttke.github.io/comdirect-api/badges/jacoco.svg)

## Motivation

There are (at least) three great Comdirect REST API libraries:

* https://github.com/jsattler/go-comdirect (Go)
* https://github.com/keisentraut/python-comdirect-api (Python)
* https://github.com/d-oit/Doit.Comdirect.Rest.Api (C#/.NET)

These libraries heavily inspired the classes in this repository.
Why another library?

* I wanted to integrate the API in a Java application and did not want to bundle the app with another "foreign language" package.
* I did not like to "callback" architecture for the challenge/response mechanism, but rather wanted to divide the login process into two stages.

## Usage

### Login process

You need a HTTP client instance and the Comdirect REST API OAuth2 credentials. 

    SimpleHttpClient myClient = new SimpleHttpClient();

    LoginCredentials credentials = new LoginCredentials(
            System.getenv("clientId"),
            System.getenv("clientSecret"),
            System.getenv("userName"),
            System.getenv("password")
    );

In the first step, you initiate the login.

    InitiateLoginService service1 = new InitiateLoginService(myClient);
    InitiateLoginResult result1 = service1.initiateLogin(credentials);
    assertEquals(ChallengeType.PUSHTAN, result1.getChallengeType());

Depending on the challenge type, after acquiring user feedback (e.g., TAN)
you can continue with the final step:

    FinalizeLoginService service2 = new FinalizeLoginService(myClient);
    ComdirectSession session = service2.finalizeLogin(credentials, result1, "");
    assertNotNull(session.getTokens().getAccessToken());

The session is then required for all other API calls.

### Retrieve accounts and balances

Acquire a list of accounts.

    AccountService service3 = new AccountService(myClient);
    AccountsPage accounts = service3.getAccounts(session);
    assertTrue(accounts.getValues().size() > 0);

### Retrieve account transactions

Read transactions for a given account.

    TransactionsPage transactions = service3.getTransactions(session, accounts.getValues().get(0).getAccountId());
    assertTrue(transactions.getValues().size() > 0);

## Contributing

I am happy to review pull requests.
How about contributing a "Depot" resource?"
