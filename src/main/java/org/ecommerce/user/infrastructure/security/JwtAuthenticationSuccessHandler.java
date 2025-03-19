package org.ecommerce.user.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ecommerce.common.jwt.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)  {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);

        // Add JWT to the response header
        response.setHeader("Authorization", "Bearer " + token);
    }
}
