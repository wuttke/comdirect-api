package eu.wuttke.comdirect.account;

import eu.wuttke.comdirect.util.Amount;

public class Account {
    private String accountId;
    private String accountDisplayId;
    private String currency;
    private String clientId;
    private AccountType accountType;
    private String iban;
    private String bic;
    private Amount creditLimit;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountDisplayId() {
        return accountDisplayId;
    }

    public void setAccountDisplayId(String accountDisplayId) {
        this.accountDisplayId = accountDisplayId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public Amount getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Amount creditLimit) {
        this.creditLimit = creditLimit;
    }
}
