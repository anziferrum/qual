package ru.anziferrum.testpay.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author anziferrum
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table(name = "merchants", uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Merchants {
    @Id
    @GenericGenerator(name="gen", strategy="increment")
    @GeneratedValue(generator="gen")
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "secret", nullable = false)
    private String secret;

    public Merchants() {
    }

    public Merchants(String name, String password, String secret) {
        this.name = name;
        this.password = password;
        this.secret = secret;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String email) {
        this.secret = email;
    }
}
