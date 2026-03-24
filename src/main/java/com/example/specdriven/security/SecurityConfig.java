package com.example.specdriven.security;

import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            configurer.loginView(LoginView.class);
        });
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var employee = User.withDefaultPasswordEncoder()
                .username("employee")
                .password("employee")
                .roles("EMPLOYEE")
                .build();
        var manager = User.withDefaultPasswordEncoder()
                .username("manager")
                .password("manager")
                .roles("MANAGER", "EMPLOYEE")
                .build();
        return new InMemoryUserDetailsManager(employee, manager);
    }
}
