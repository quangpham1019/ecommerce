package org.ecommerce.user.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
public class UserContextSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public UserContextSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean(name = "userContextResources")
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/users/**")
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/users/**").authenticated()  // Protect user endpoints
                        .anyRequest().permitAll())  // Other requests can be public
                .securityContext(c -> c.disable())  // Disable security context (for stateless APIs)
                .sessionManagement(c -> c.disable())  // Disable session creation
                .requestCache(c -> c.disable())  // Disable request cache
                .csrf(csrf -> csrf.disable())
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
