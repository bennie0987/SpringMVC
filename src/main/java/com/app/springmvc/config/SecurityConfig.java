package com.app.springmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/home", "/error", "/secure", "/products/**", "/geo/**").permitAll() // Public pages
                .requestMatchers("/").authenticated()
                .anyRequest().authenticated() // All other pages require authentication
            )
            .formLogin(formLogin -> formLogin
//                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("/", true)
                .permitAll() // Allow access to login page
            )
            .logout(logout -> logout
                .permitAll() // Allow logout access
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/access-denied") // Custom access denied page
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
