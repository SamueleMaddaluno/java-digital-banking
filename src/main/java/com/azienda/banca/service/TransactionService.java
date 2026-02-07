package com.azienda.banca.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azienda.banca.model.Account;
import com.azienda.banca.model.Transaction;
import com.azienda.banca.repository.AccountRepository;
import com.azienda.banca.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void executeTransaction(Account account, BigDecimal amount, String type, String description){
        if("DEPOSIT".equals(type)){
            account.setBalance(account.getBalance().add(amount));
        }
        else if("WITHDRAW".equals(type)){
            if(amount.compareTo(account.getBalance())>0){
                throw new RuntimeException("Saldo sul conto insufficente");
            }else{
                account.setBalance(account.getBalance().subtract(amount));
            }
        }
        accountRepository.save(account);

        Transaction transaction= new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(type);
        transaction.setDescription(description);

        transactionRepository.save(transaction);
    }

    public List<Transaction> getHistoy(Account account){
        return transactionRepository.findByAccountOrderByTimestampDesc(account);
    }

}

