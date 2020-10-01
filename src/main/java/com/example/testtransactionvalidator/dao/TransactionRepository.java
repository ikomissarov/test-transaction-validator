package com.example.testtransactionvalidator.dao;

import com.example.testtransactionvalidator.model.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findAllByIban(String iban);

    @Query(value = "select iban from account where blacklisted = true", nativeQuery = true)
    List<String> findBlackListedAccounts();
}
