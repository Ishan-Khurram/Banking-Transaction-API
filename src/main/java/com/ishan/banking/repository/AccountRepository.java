package com.ishan.banking.repository;
import com.ishan.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    // includes the crud implementations we need, like findByID, and save()
}
