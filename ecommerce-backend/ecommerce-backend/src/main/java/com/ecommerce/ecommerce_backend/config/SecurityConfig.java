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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
               .authorizeHttpRequests(auth -> auth

    // AUTH
    .requestMatchers("/auth/**").permitAll()

    // PRODUCTS (PUBLIC + ADMIN)
    .requestMatchers(
        "/products",
        "/products/{id}",
        "/products/search",
        "/products/filter",
        "/products/{id}/recommendations"
    ).permitAll()
    .requestMatchers("/products/**").hasRole("ADMIN")

    // USER FEATURES
    .requestMatchers(
        "/cart/**",
        "/wishlist/**",
        "/address/**"
    ).hasAnyRole("USER","ADMIN")

    // ORDERS
    .requestMatchers(
        "/orders/place",
        "/orders/my",
        "/orders/{orderId}",
        "/orders/invoice/**"
    ).hasAnyRole("USER","ADMIN")

    // COUPONS (USER)
    .requestMatchers(
        "/coupon/gift",
        "/coupon/validate/**",
        "/coupon/my"
    ).hasAnyRole("USER","ADMIN")

    // ADMIN
    .requestMatchers("/admin/**").hasRole("ADMIN")

    // EVERYTHING ELSE
    .anyRequest().authenticated()
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

    @Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(List.of("http://localhost:4200"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
}

}
