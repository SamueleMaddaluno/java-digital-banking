package com.azienda.banca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azienda.banca.model.Account;
import com.azienda.banca.model.User;

public interface AccountRepository extends JpaRepository<Account, Long>{

   List<Account> findByUser(User user);

   Optional<Account>findByIban(String iban);

}
