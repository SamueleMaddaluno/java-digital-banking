package com.azienda.banca.controller;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azienda.banca.model.Account;
import com.azienda.banca.model.User;
import com.azienda.banca.repository.AccountRepository;
import com.azienda.banca.repository.UserRepository;
import com.azienda.banca.service.AccountService;
import com.azienda.banca.service.TransactionService;
import com.azienda.banca.service.UserService;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user){
        userService.saveUser(user);
        return "redirect:/login";

    }
    
    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
    }    

    @GetMapping("/dashboard")
    public String showDashboard(Authentication authentication, Model model){
        String username=authentication.getName();
        User user= userRepository.findByUsername(username).orElseThrow();
        List<Account> accounts= accountService.getAccountByUser(user);
        
        model.addAttribute("accounts", accounts);
        model.addAttribute("username", username);

        return "dashboard";
    }
    
    @PostMapping("/account/create")
    public String openAccount(Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        accountService.createAccount(user, "CORRENTE");
        return "redirect:/dashboard";
    }

    @PostMapping("/accounts/transaction")
    public String transaction(@RequestParam String iban,
                              @RequestParam BigDecimal amount,
                              @RequestParam String type,
                              @RequestParam String description){

        Account account= accountRepository.findByIban(iban).orElseThrow(() -> new RuntimeException("Account non trovato"));

        transactionService.executeTransaction(account, amount, type, description);

        return "redirect:/dashboard";


    }

}


