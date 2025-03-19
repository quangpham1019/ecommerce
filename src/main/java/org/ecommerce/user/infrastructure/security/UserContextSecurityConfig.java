package org.ecommerce.user.infrastructure.security;

import org.ecommerce.common.config.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class UserContextSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CorsConfig corsConfig;
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    public UserContextSecurityConfig(UserDetailsService userDetailsService,
                                     CorsConfig corsConfig,
                                     JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.corsConfig = corsConfig;
        this.jwtAuthenticationSuccessHandler = jwtAuthenticationSuccessHandler;
    }

    @Bean(name = "userContextResources")
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/users/**").authenticated()  // Protect user endpoints
                        .anyRequest().permitAll())  // Other requests can be public
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Enable session-based auth
                        .sessionFixation().migrateSession() // Prevent session hijacking
                        .maximumSessions(1) // Optional: limit to one session per user
                        .expiredUrl("/login") // Optional: redirect on session expiration
                )
                .formLogin(form -> form
                        .successHandler(jwtAuthenticationSuccessHandler)
//                        .defaultSuccessUrl("/api/users", true)  // Redirect after login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .requestCache(RequestCacheConfigurer::disable)  // Disable request cache (if not required)
                .csrf(Customizer.withDefaults()) // Enable CSRF protection for session-based auth
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) // Enable CORS
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Using BCryptPasswordEncoder for password encoding
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setHideUserNotFoundExceptions(false); // Ensures exception is thrown when user not found
        return authProvider;
    }
}
