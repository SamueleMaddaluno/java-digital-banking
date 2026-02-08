package com.azienda.banca.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotBlank(message="Username is necessary")
    @Size(min=4, max=20, message="Username must be beetwen 4 and 20 characters")
    @Column(unique=true, nullable=false)
    private String username;

    @Email(message="Enter a valid email address")
    @NotBlank(message="Email is required")
    @Column(unique=true, nullable=false)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private String role= "ROLE_USER";
}
