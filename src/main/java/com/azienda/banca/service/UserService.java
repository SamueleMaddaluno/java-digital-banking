package com.azienda.banca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.azienda.banca.model.User;
import com.azienda.banca.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository usereRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user){
        String encodedPassword= passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return usereRepository.save(user);
    }

}
