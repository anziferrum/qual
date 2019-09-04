package ru.anziferrum.testpay.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author anziferrum
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "tokens", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class Tokens {
    @Id
    @GenericGenerator(name="gen", strategy="increment")
    @GeneratedValue(generator="gen")
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;
    @Column(name = "token", nullable = false, length = 100)
    private String token;
    @Column(name = "expirationDate", nullable = false)
    private Date expirationDate;

    public Tokens() {
    }

    public Tokens(String username, String token, Date expirationDate) {
        this.username = username;
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
