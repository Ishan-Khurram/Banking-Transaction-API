package com.ishan.banking.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ishan.banking.model.Account;
import com.ishan.banking.model.Transaction;
import com.ishan.banking.model.TransactionType;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import com.ishan.banking.repository.TransactionRepository;
import com.ishan.banking.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    // create account and return account
    public Account createAccount(String ownerName){
        Account newAccount = new Account(ownerName, new BigDecimal("0.00"));
        Account savedAccount = accountRepository.save(newAccount);

        return savedAccount;

    }
    // deposit money into account return updated account
    public Account deposit(Long id, BigDecimal despoitedAmount){
        // get account via id
        Account updateAccount = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found."));
        // add balance with deposited amount
        BigDecimal updatedBalance = updateAccount.getBalance().add(despoitedAmount);
        // update account balance
        updateAccount.setBalance(updatedBalance);
        // log Transaction
        transactionRepository.save(new Transaction(updateAccount, TransactionType.DEPOSIT, despoitedAmount));
        // return updated account
        return accountRepository.save(updateAccount);
    }

    // withdraw money from account return updated account
    public Account withdraw(Long id, BigDecimal withdrawedAmount) {
        // get account via id
        Account updateAccount = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found."));
        // check for insufficient funds:
        if (updateAccount.getBalance().compareTo(withdrawedAmount) < 0) {
            throw new RuntimeException("Insufficient funds for this transaction");
        }
        // get current balance, and subtract the withdrawal amount
        BigDecimal updatedBalance = updateAccount.getBalance().subtract(withdrawedAmount);
        // update account
        updateAccount.setBalance(updatedBalance);
        // log transaction
        transactionRepository.save(new Transaction(updateAccount, TransactionType.WITHDRAWAL, withdrawedAmount));
        // return updated account
        return accountRepository.save(updateAccount);

    }
    // transfer money from one account to another
    public List<Account> transfer(Long sourceId, Long destinationId, BigDecimal transferedAmount){
        // fetch source account by id
        Account sourceAccount = accountRepository.findById(sourceId).orElseThrow(() -> new RuntimeException("Account not found."));
        // fetch destination account by id
        Account destinationAccount = accountRepository.findById(destinationId).orElseThrow(() -> new RuntimeException("Account not found."));
        // Check for sufficient funds within source account
        if (sourceAccount.getBalance().compareTo(transferedAmount) < 0) {
            throw new RuntimeException("Insufficient funds for this transaction.");
        }
        // subtract transfered amount from source account
        BigDecimal updatedSourceBalance = sourceAccount.getBalance().subtract(transferedAmount);
        // add funds to destination account
        BigDecimal updatedDestinationBalance = destinationAccount.getBalance().add(transferedAmount);
        // update source account and destination account
        sourceAccount.setBalance(updatedSourceBalance);
        destinationAccount.setBalance(updatedDestinationBalance);
        // save accounts
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        // save transaction trail
        transactionRepository.save(new Transaction(sourceAccount, TransactionType.TRANSFER, transferedAmount));
        transactionRepository.save(new Transaction(destinationAccount, TransactionType.TRANSFER, transferedAmount));
        // create list and add both accounts to list
        List<Account> accountList = new ArrayList<>();
        accountList.add(sourceAccount);
        accountList.add(destinationAccount);

        // return the list of accounts
        return accountList;
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found."));
    }
    
    public List<Transaction> getTransactionHistory(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found."));

        return transactionRepository.findByAccount(account);
    }
}
