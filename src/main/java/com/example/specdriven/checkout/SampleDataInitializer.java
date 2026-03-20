package com.example.specdriven.checkout;

import com.example.specdriven.security.User;
import com.example.specdriven.security.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SampleDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SampleDataInitializer(ProductRepository productRepository,
                                  CustomerRepository customerRepository,
                                  UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.save(new Product("4006381333931", "Nivea Creme", new BigDecimal("3.49")));
            productRepository.save(new Product("5000159484695", "Coca-Cola 1.5L", new BigDecimal("1.89")));
            productRepository.save(new Product("4005808262152", "Ritter Sport Chocolate", new BigDecimal("1.29")));
            productRepository.save(new Product("8710447327050", "Heineken 6-Pack", new BigDecimal("5.99")));
            productRepository.save(new Product("3017620422003", "Nutella 750g", new BigDecimal("4.99")));
            productRepository.save(new Product("7622210449283", "Oreo Cookies", new BigDecimal("2.19")));
        }

        if (customerRepository.count() == 0) {
            customerRepository.save(new Customer("Alice Johnson", "CARD001"));
            customerRepository.save(new Customer("Bob Smith", "CARD002"));
            customerRepository.save(new Customer("Clara Mueller", "CARD003"));
        }

        if (userRepository.count() == 0) {
            userRepository.save(new User("admin", passwordEncoder.encode("admin"), "ADMIN"));
        }
    }
}
