package com.fitflow.clover.global.security.config;

import com.fitflow.clover.domain.admin.service.AdminDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class AdminSecurityConfig {
    private final AdminDetailsService adminDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider adminProvider = new DaoAuthenticationProvider(adminDetailsService);
        adminProvider.setPasswordEncoder(passwordEncoder);

        ProviderManager adminAuthenticationManager = new ProviderManager(adminProvider);

        http
                .securityMatchers(matchers -> matchers.requestMatchers(
                        "/login", "/signup", "/logout", "/login-process", "/",
                        "/reports", "/reports/**",
                        "/communities", "/communities/**",
                        "/css/**", "/js/**", "/images/**", "/favicon.ico"
                ))
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(adminAuthenticationManager)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        .requestMatchers("/login", "/signup").permitAll()
                        .requestMatchers("/reports", "/reports/**", "/communities", "/communities/**").hasRole("ADMIN")
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login-process")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                );

        return http.build();
    }
}