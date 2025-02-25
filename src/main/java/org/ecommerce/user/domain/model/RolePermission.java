package org.ecommerce.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.ecommerce.user.domain.model.enums.RolePermissionStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    @ToString.Exclude
    private Permission permission;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @ToString.Exclude
    private Role role;

    @CreationTimestamp
    private Timestamp assignedAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private RolePermissionStatus status;

    public RolePermission(Permission permission, Role role, RolePermissionStatus rolePermissionStatus) {
        this.permission = permission;
        this.role = role;
        this.status = rolePermissionStatus;
    }
}
