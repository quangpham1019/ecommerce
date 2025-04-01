package org.ecommerce.user.infrastructure.security;

import org.ecommerce.common.config.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class UserContextSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CorsConfig corsConfig;
    private final CustomSuccessHandler customSuccessHandler;

    public UserContextSecurityConfig(UserDetailsService userDetailsService,
                                     CorsConfig corsConfig,
                                     CustomSuccessHandler customSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.corsConfig = corsConfig;
        this.customSuccessHandler = customSuccessHandler;
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
                        .successHandler(customSuccessHandler)
//                        .defaultSuccessUrl("/swagger-ui/index.html#/", true)  // Redirect after login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .requestCache(RequestCacheConfigurer::disable)  // Disable request cache (if not required)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())) // Enable CSRF protection for session-based auth
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
