package org.ecommerce.product.infrastructure.security;

import org.ecommerce.common.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
public class ProductContextSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public ProductContextSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean(name = "productContextResources")
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/products/**")
                .authorizeHttpRequests(c -> c
                        .anyRequest().permitAll())
                .securityContext(AbstractHttpConfigurer::disable)  // Disable security context (for stateless APIs)
                .sessionManagement(AbstractHttpConfigurer::disable)  // Disable session creation
                .requestCache(RequestCacheConfigurer::disable)  // Disable request cache (if not needed)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
