package org.ecommerce.user.application.service;

import org.ecommerce.user.application.dto.authDTO.LoginDTO;
import org.ecommerce.user.application.dto.authDTO.LoginResponseDTO;
import org.ecommerce.user.infrastructure.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    public LoginResponseDTO login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return new LoginResponseDTO(jwtUtil.generateToken(userDetails));
    }
}
