package org.ecommerce.user.infrastructure.security;

import org.ecommerce.common.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
//@EnableWebSecurity
public class UserContextSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final CorsConfig corsConfig;

    public UserContextSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService, CorsConfig corsConfig) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.corsConfig = corsConfig;
    }

    @Bean(name = "userContextResources")
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/users/**")
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/users/**").authenticated()  // Protect user endpoints
                        .anyRequest().permitAll())  // Other requests can be public
                .securityContext(AbstractHttpConfigurer::disable)  // Disable security context (for stateless APIs)
                .sessionManagement(AbstractHttpConfigurer::disable)  // Disable session creation
                .requestCache(RequestCacheConfigurer::disable)  // Disable request cache
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) // Enable CORS
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
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

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http,
//                                            OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler,
//                                            OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler) throws Exception {
//        return http
//                .formLogin(c -> c.loginPage("/api/v1/auth/signInPage")
//                        .loginProcessingUrl("/api/v1/auth/signInProcess")
//                        .usernameParameter("user")
//                        .passwordParameter("pass")
//                        .defaultSuccessUrl("/inventory")
//                )
//                .logout(c -> c.logoutSuccessUrl("/?logout"))
//                .oauth2Login(oc -> oc
//                        .loginPage("/api/v1/auth/signInPage")
//                        .defaultSuccessUrl("/inventory")
//                        .userInfoEndpoint(ui -> ui
//                                .userService(oauth2LoginHandler)
//                                .oidcUserService(oidcLoginHandler)))
//                .authorizeHttpRequests(c -> c
//                        .requestMatchers(EndpointRequest.to("info", "health", "prometheus")).permitAll()
//                        .requestMatchers(EndpointRequest.toAnyEndpoint().excluding("info", "health", "prometheus")).hasAuthority("manage")
//                        .requestMatchers("/", "/login", "/user/sign-up", "/error").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .build();
//    }

}
