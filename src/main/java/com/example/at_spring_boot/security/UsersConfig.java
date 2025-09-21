package com.example.at_spring_boot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("prod")
public class UsersConfig {

    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        var prof = User.withUsername("prof")
                .password(encoder.encode("prof")) // credenciais de PROD
                .roles("PROFESSOR")
                .build();
        return new InMemoryUserDetailsManager(prof);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}