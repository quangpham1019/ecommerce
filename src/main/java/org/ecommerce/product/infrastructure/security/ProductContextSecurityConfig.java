package org.ecommerce.product.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity
public class ProductContextSecurityConfig {

    @Bean(name = "productContextResources")
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/products/**")
                .authorizeHttpRequests(c -> c.anyRequest().permitAll())
                .securityContext(c -> c.disable())
                .sessionManagement(c -> c.disable())
                .requestCache(c -> c.disable())
                .build();
    }

//
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
