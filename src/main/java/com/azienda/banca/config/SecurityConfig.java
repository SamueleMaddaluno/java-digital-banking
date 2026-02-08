// package com.azienda.banca.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// import com.azienda.banca.security.CustomUserDetailsService;

// @Configuration
// public class SecurityConfig {

//     @Autowired
//     private CustomUserDetailsService userDetailsService;

//     @Bean
//     public PasswordEncoder passwordEncoder(){
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//         http
//             .csrf(csrf->csrf.ignoringRequestMatchers("/api/**"))
//             .authorizeHttpRequests(auth->auth
//                 .requestMatchers("/","/register","/login","/css/**").permitAll()
//                 .anyRequest().authenticated()
//             )
//             .userDetailsService(userDetailsService)
//             .formLogin(form-> form
//                 .loginPage("/login")
//                 .defaultSuccessUrl("/dashboard", true)
//                 .permitAll()
//             )
//             .logout(logout -> logout.permitAll());

//         return http.build();
//     }

// }


package com.azienda.banca.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order; // Import necessario per l'ordine
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.azienda.banca.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- 1. CATENA DI FILTRI PER LE API REST ---
    @Bean
    @Order(1) // Diamo priorità a questa catena
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
        .securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable()) // Disabilitiamo CSRF per le API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").authenticated() // Tutte le API richiedono autenticazione
            )
            .userDetailsService(userDetailsService)
            .httpBasic(Customizer.withDefaults()); // Usiamo l'autenticazione Basic (per Postman)

        return http.build();
    }

    // --- 2. CATENA DI FILTRI PER IL SITO WEB (HTML) ---
    @Bean
    @Order(2) // Questa catena è la seconda opzione
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/register", "/login", "/css/**").permitAll()
                .anyRequest().authenticated()
            )
            .userDetailsService(userDetailsService)
            .formLogin(form -> form // Usiamo il form login per il sito
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}
