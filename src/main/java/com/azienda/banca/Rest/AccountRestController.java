package com.azienda.banca.Rest;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azienda.banca.model.Account;
import com.azienda.banca.model.Transaction;
import com.azienda.banca.model.User;
import com.azienda.banca.repository.AccountRepository;
import com.azienda.banca.repository.UserRepository;
import com.azienda.banca.service.AccountService;
import com.azienda.banca.service.TransactionService;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/my-accounts")
    public List<Account>accounts(Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return accountService.getAccountByUser(user);
    }

    @GetMapping("/{iban}/transaction")
    public List<Transaction>transactions(@PathVariable String iban, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Account account = accountRepository.findByIban(iban)
                    .orElseThrow(()-> new RuntimeException("conto non trovato"));

        if(!account.getUser().getUsername().equals(authentication.getName())){
            throw new RuntimeException("accesso negato");
        }

        return transactionService.getHistoy(account);
    }

    @PostMapping("/{iban}/transaction")
    public String executeTransaction(Authentication authentication,
                                     @RequestParam BigDecimal amount,
                                     @RequestParam String type,
                                     @RequestParam String description,
                                     @PathVariable String iban){


        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Account account = accountRepository.findByIban(iban)
            .orElseThrow(()-> new RuntimeException("conto non trovato"));
        if(!account.getUser().getUsername().equals(authentication.getName())){
            return ("accesso negato");
        }
        transactionService.executeTransaction(account, amount, type, description);
        return "transazione eseguita correttamente";
    }






}
