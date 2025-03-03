package org.ecommerce.user.api.controller;
import org.ecommerce.user.application.dto.authDTO.LoginDTO;
import org.ecommerce.user.application.dto.authDTO.LoginResponseDTO;
import org.ecommerce.user.application.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


//    @PostMapping("/login")
//    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginRequest) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated()) {
//            throw new AuthorizationDeniedException("User is already authenticated");
//        }
//        return ResponseEntity.ok(authService.login(loginRequest));
//    }
}
