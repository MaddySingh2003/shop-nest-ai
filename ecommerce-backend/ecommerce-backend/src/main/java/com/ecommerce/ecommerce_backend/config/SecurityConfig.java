package com.ecommerce.ecommerce_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers("/products").permitAll()
.requestMatchers("/products/{id}").permitAll()
.requestMatchers("/products/**").hasRole("ADMIN")
.requestMatchers("/products/add","/products/update/**","/products/delete/**").hasRole("ADMIN")
 .requestMatchers("/orders/all").hasRole("ADMIN")
.requestMatchers("/orders/update/**").hasRole("ADMIN")
.requestMatchers("/orders/*/admin").hasRole("ADMIN")
.requestMatchers("/orders/place/**").hasRole("USER")
.requestMatchers("/orders/cancel/**").hasRole("USER")
.requestMatchers("/orders/my").hasRole("USER")
.requestMatchers("/orders/**").hasRole("USER")       .requestMatchers("/cart/**").hasRole("USER")
        .requestMatchers("/user/**").hasRole("USER")
        .requestMatchers("/address/**").hasRole("USER")
.requestMatchers("/payment/**").hasRole("USER")
.requestMatchers("/wishlist/**").hasRole("USER")
.requestMatchers("/reviews/**").hasRole("USER")
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/coupon/**").hasRole("USER")
        .anyRequest().permitAll()
)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
