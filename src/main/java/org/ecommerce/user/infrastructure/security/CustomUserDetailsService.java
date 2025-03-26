package org.ecommerce.user.infrastructure.security;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ArrayUtils;
import org.ecommerce.user.domain.model.Role;
import org.ecommerce.user.domain.model.UserRole;
import org.ecommerce.user.infrastructure.repository.jpa.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<org.ecommerce.user.domain.model.User> user = userRepository.findByEmail_Address(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        List<Role> roles = user
                .get()
                .getUserRoles()
                .stream()
                .map(UserRole::getRole)
                .toList();

        String[] roleList = roles
                .stream()
                .map(role -> "ROLE_"+role.getRoleName())
                .toArray(String[]::new);

        String[] permissionList = roles
                .stream()
                .map(role -> role.getRolePermissions().stream().toList())
                .flatMap(Collection::stream)
                .map(rolePermission -> rolePermission.getPermission().getPermissionName())
                .collect(Collectors.toSet())
                .toArray(String[]::new);

        return User.withUsername(user.get().getId().toString()) // Use userId as username
                .password(user.get().getPassword()) // Assume password is already hashed
//                .roles(roleNames)         // get overridden by .authorities(), Spring use GRANTED_AUTHORITIES for both roles and authorities
                .authorities(ArrayUtils.addAll(permissionList, roleList))
                .build();
    }
}

