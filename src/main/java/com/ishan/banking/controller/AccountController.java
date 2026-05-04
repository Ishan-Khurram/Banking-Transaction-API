package com.ishan.banking.controller;
import com.ishan.banking.dto.TransferRequest;
import com.ishan.banking.model.Account;
import com.ishan.banking.model.Transaction;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.ishan.banking.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    // get account
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id){
        return accountService.getAccount(id);
    }

    // get transaction history
    @GetMapping("/{id}/transaction-history")
    public List<Transaction> getTransactionHistory(@PathVariable Long id) {
        return accountService.getTransactionHistory(id);
    }

    // post deposit
    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestBody BigDecimal amount) {
        return accountService.deposit(id, amount);
    }

    // post account
    @PostMapping
    public Account createAccount(@RequestBody String ownerName){
        return accountService.createAccount(ownerName);
    }

    // post withdraw
    @PostMapping("/{id}/withdrawal")
    public Account withdrawal(@PathVariable Long id, @RequestBody BigDecimal amount) {
        return accountService.withdraw(id, amount);
    }

    // post transfer
    @PostMapping("/transfer")
    public List<Account> transfers(@RequestBody TransferRequest request){
        return accountService.transfer(request.getSourceId(), request.getDestinationId(), request.getAmount());
    }

}
