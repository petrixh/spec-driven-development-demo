package com.example.specdriven.security;

import com.example.specdriven.domain.Role;
import com.example.specdriven.domain.User;
import com.example.specdriven.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User("Alice Customer", "customer@test.com",
                    passwordEncoder.encode("customer@test.com"), Role.CUSTOMER));
            userRepository.save(new User("Bob Agent", "agent@test.com",
                    passwordEncoder.encode("agent@test.com"), Role.ADMIN));
            userRepository.save(new User("Carol Manager", "manager@test.com",
                    passwordEncoder.encode("manager@test.com"), Role.ADMIN));
        }
    }
}
