package com.example.specdriven.security;

import com.example.specdriven.domain.UserRepository;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            var appUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

            var authorities = switch (appUser.getRole()) {
                case ADMIN -> List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ROLE_USER"));
                case CUSTOMER -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
            };

            return new User(appUser.getEmail(), appUser.getPassword(), authorities);
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.with(VaadinSecurityConfigurer.vaadin(), vaadin -> {
            vaadin.loginView(LoginView.class);
        });
        http.logout(logout -> logout.logoutSuccessUrl("/login"));
        return http.build();
    }
}
