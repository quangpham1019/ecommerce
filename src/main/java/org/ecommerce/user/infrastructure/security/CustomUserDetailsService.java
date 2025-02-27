package org.ecommerce.user.infrastructure.security;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<org.ecommerce.user.domain.model.User> user = userRepository.findByEmail_Address(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(user.get().getEmail().getAddress()) // Use email as username
                .password(user.get().getPassword()) // Assume password is already hashed
//                .roles("USER")
                .build();
    }
}

