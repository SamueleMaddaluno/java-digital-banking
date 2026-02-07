package com.azienda.banca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azienda.banca.model.Account;
import com.azienda.banca.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    List<Transaction>findByAccountOrderByTimestampDesc(Account account);

}
