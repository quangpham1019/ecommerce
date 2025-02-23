package org.ecommerce.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.ecommerce.user.domain.model.enums.UserRoleStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Include
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    @EqualsAndHashCode.Include
    private Role role;

    @CreationTimestamp
    private Timestamp assignedAt;

    @Enumerated(EnumType.STRING)
    private UserRoleStatus status;

    public UserRole(User user, Role role, UserRoleStatus status) {
        this.user = user;
        this.role = role;
        this.status = status;
    }

    public void deactivate() {
        this.status = UserRoleStatus.INACTIVE;
    }

    public void activate() {
        this.status = UserRoleStatus.ACTIVE;
    }
}
