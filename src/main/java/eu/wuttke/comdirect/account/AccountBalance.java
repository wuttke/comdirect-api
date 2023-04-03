package eu.wuttke.comdirect.account;

import eu.wuttke.comdirect.value.Amount;

public class AccountBalance {

    private Account account;
    private String accountId;
    private Amount balance;
    private Amount balanceEUR;
    private Amount availableCashAmount;
    private Amount availableCashAmountEUR;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    public Amount getBalanceEUR() {
        return balanceEUR;
    }

    public void setBalanceEUR(Amount balanceEUR) {
        this.balanceEUR = balanceEUR;
    }

    public Amount getAvailableCashAmount() {
        return availableCashAmount;
    }

    public void setAvailableCashAmount(Amount availableCashAmount) {
        this.availableCashAmount = availableCashAmount;
    }

    public Amount getAvailableCashAmountEUR() {
        return availableCashAmountEUR;
    }

    public void setAvailableCashAmountEUR(Amount availableCashAmountEUR) {
        this.availableCashAmountEUR = availableCashAmountEUR;
    }
}
