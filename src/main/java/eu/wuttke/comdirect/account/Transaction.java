package eu.wuttke.comdirect.account;

import eu.wuttke.comdirect.util.Amount;
import eu.wuttke.comdirect.util.CodedValue;

public class Transaction {

    private String reference;
    private String bookingStatus;
    private String bookingDate;
    private Amount amount;
    private BankContact remitter;
    private BankContact deptor;
    private BankContact creditor;
    private String valutaDate;
    private String directDebitCreditorId;
    private String directDebitMandateId;
    private String endToEndReference;
    private boolean newTransaction;
    private String remittanceInfo;
    private CodedValue transactionType;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public BankContact getRemitter() {
        return remitter;
    }

    public void setRemitter(BankContact remitter) {
        this.remitter = remitter;
    }

    public BankContact getDeptor() {
        return deptor;
    }

    public void setDeptor(BankContact deptor) {
        this.deptor = deptor;
    }

    public BankContact getCreditor() {
        return creditor;
    }

    public void setCreditor(BankContact creditor) {
        this.creditor = creditor;
    }

    public String getValutaDate() {
        return valutaDate;
    }

    public void setValutaDate(String valutaDate) {
        this.valutaDate = valutaDate;
    }

    public String getDirectDebitCreditorId() {
        return directDebitCreditorId;
    }

    public void setDirectDebitCreditorId(String directDebitCreditorId) {
        this.directDebitCreditorId = directDebitCreditorId;
    }

    public String getDirectDebitMandateId() {
        return directDebitMandateId;
    }

    public void setDirectDebitMandateId(String directDebitMandateId) {
        this.directDebitMandateId = directDebitMandateId;
    }

    public String getEndToEndReference() {
        return endToEndReference;
    }

    public void setEndToEndReference(String endToEndReference) {
        this.endToEndReference = endToEndReference;
    }

    public boolean isNewTransaction() {
        return newTransaction;
    }

    public void setNewTransaction(boolean newTransaction) {
        this.newTransaction = newTransaction;
    }

    public String getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(String remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }

    public CodedValue getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(CodedValue transactionType) {
        this.transactionType = transactionType;
    }
}
