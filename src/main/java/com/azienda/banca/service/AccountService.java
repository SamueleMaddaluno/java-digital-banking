package com.azienda.banca.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azienda.banca.model.Account;
import com.azienda.banca.model.User;
import com.azienda.banca.repository.AccountRepository;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(User user, String type){
        Account account= new Account();
        account.setIban(generateIban());
        account.setBalance(BigDecimal.ZERO);
        account.setAccountType(type);
        account.setUser(user);

        return accountRepository.save(account);
    }

    public List<Account>getAccountByUser(User user){
        return accountRepository.findByUser(user);
    }

    private String generateIban(){
        Random ran = new Random();
        StringBuilder sb = new StringBuilder("IT");
        for(int i=0;i<25;i++){
            sb.append(ran.nextInt(10));
        }
        return sb.toString();
    }

    
}
