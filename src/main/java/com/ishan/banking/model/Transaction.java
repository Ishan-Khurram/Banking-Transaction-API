package com.ishan.banking.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

    // constructors
    public Transaction() {}
    public Transaction(Account account, TransactionType type, BigDecimal amount) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }


    // instance Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    // behaviours
    public Long getId() { 
        return id; 
    }

    public Account getAccount() { 
        return account; 
    }
    
    public TransactionType getType() { 
        return type; 
    }

    public BigDecimal getAmount() { 
        return amount; 
    }

    public LocalDateTime getTimestamp() { 
        return timestamp; 
    }
}
