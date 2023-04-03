package eu.wuttke.comdirect.account;

import java.util.Date;

public class TransactionAggregation {

    private String accountId;
    private String bookingDateLatestTransaction;
    private String referenceLatestTransaction;
    private boolean latestTransactionIncluded;
    private Date pagingTimestamp;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBookingDateLatestTransaction() {
        return bookingDateLatestTransaction;
    }

    public void setBookingDateLatestTransaction(String bookingDateLatestTransaction) {
        this.bookingDateLatestTransaction = bookingDateLatestTransaction;
    }

    public String getReferenceLatestTransaction() {
        return referenceLatestTransaction;
    }

    public void setReferenceLatestTransaction(String referenceLatestTransaction) {
        this.referenceLatestTransaction = referenceLatestTransaction;
    }

    public boolean isLatestTransactionIncluded() {
        return latestTransactionIncluded;
    }

    public void setLatestTransactionIncluded(boolean latestTransactionIncluded) {
        this.latestTransactionIncluded = latestTransactionIncluded;
    }

    public Date getPagingTimestamp() {
        return pagingTimestamp;
    }

    public void setPagingTimestamp(Date pagingTimestamp) {
        this.pagingTimestamp = pagingTimestamp;
    }
}
