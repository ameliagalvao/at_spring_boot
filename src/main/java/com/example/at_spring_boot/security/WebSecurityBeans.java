package com.example.at_spring_boot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityBeans {

    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        UserDetails professor = User.withUsername("prof")
                .password(encoder.encode("123"))
                .roles("PROFESSOR")
                .build();

        UserDetails aluno = User.withUsername("aluno")
                .password(encoder.encode("123"))
                .roles("ALUNO")
                .build();

        return new InMemoryUserDetailsManager(professor, aluno);
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
