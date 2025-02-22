package org.ecommerce.user.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    private Long id;

    private String username;
    private String password;

    @EqualsAndHashCode.Include
    private Email email;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<UserRole> userRoles;

    public User(String username, String password, Email email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserRole addRole(Role role) {

        Optional<UserRole> existingRole = userRoles.stream().filter(userRole -> userRole.getRole().equals(role)).findFirst();

        if (existingRole.isPresent()) {
            UserRole currentUserRole = existingRole.get();

            if (currentUserRole.getStatus().equals(UserRoleStatus.ACTIVE)) throw new IllegalArgumentException("User already has this role");

            currentUserRole.activate();
            return currentUserRole;
        } else {
            UserRole newUserRole = new UserRole(this, role, UserRoleStatus.ACTIVE);
            userRoles.add(newUserRole);
            return newUserRole;
        }
    }

    public Set<UserRole> getUserRoles() {
        return userRoles.stream().filter(ur -> ur.getStatus() == UserRoleStatus.ACTIVE).collect(Collectors.toSet());
    }
}
