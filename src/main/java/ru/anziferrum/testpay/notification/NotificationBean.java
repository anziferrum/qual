package ru.anziferrum.testpay.notification;

import ru.anziferrum.testpay.enums.TransactionState;

import java.math.BigDecimal;

/**
 * @author anziferrum
 */
public class NotificationBean {
    private String currency;
    private BigDecimal amount;
    private String id;
    private String externalId;
    private TransactionState transactionState;
    private String notificationURL;
    private String signature;
    private int executed = 0;

    public NotificationBean(String currency, BigDecimal amount, String id, String externalId, TransactionState transactionState, String notificationURL, String signature) {
        this.currency = currency;
        this.amount = amount;
        this.id = id;
        this.externalId = externalId;
        this.transactionState = transactionState;
        this.notificationURL = notificationURL;
        this.signature = signature;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public TransactionState getTransactionState() {
        return transactionState;
    }

    public void setTransactionState(TransactionState transactionState) {
        this.transactionState = transactionState;
    }

    public String getNotificationURL() {
        return notificationURL;
    }

    public void setNotificationURL(String notificationURL) {
        this.notificationURL = notificationURL;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void inc() {
        executed++;
    }

    public int getExecuted() {
        return executed;
    }
}
