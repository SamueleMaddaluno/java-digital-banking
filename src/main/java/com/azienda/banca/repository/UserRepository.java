package com.azienda.banca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azienda.banca.model.User;


public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User>findByUsername(String username);
    
}
