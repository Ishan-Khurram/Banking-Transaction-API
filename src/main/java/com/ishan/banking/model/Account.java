package com.ishan.banking.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// The schema.
// Needs id, ownerName, balance, and createdAt

// tells the DB this is a database table
@Entity
@Table(name = "accounts")
public class Account {

    // constructors
    public Account() {}

    public Account(String ownerName, BigDecimal balance) {
        this.ownerName = ownerName;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }
    // instance variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generate completely random id for each user.
    private Long id;
    private String ownerName;
    private BigDecimal balance; // exact decimal precision
    private LocalDateTime createdAt;

    // getters and setters needed.

    // getters for the instance variables
    public Long getId() {
        return this.id;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    // setters for implementing data
    public void setOwnerName(String newName) {
        this.ownerName = newName;
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

}
