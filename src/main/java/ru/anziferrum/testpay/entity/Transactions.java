package ru.anziferrum.testpay.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author anziferrum
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "transactions", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Transactions {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    @Column(name = "payerEmail", nullable = false, length = 100)
    private String payerEmail;
    @Column(name = "creationDate", nullable = false)
    private Date creationDate;
    @Column(name = "externalId", nullable = false)
    private String externalId;
    @Column(name = "state", nullable = false)
    private String state;

    public Transactions() {
    }

    public Transactions(String id, BigDecimal amount, String currency, String payerEmail, Date creationDate, String externalId, String state) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.payerEmail = payerEmail;
        this.creationDate = creationDate;
        this.externalId = externalId;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payer) {
        this.payerEmail = payer;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
