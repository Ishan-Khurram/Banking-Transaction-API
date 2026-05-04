package com.ishan.banking.repository;
import com.ishan.banking.model.Transaction;
import com.ishan.banking.model.Account;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findByAccount(Account account);
    
}
