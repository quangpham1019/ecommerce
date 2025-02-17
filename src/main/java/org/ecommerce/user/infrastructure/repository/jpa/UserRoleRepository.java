package org.ecommerce.user.infrastructure.repository.jpa;

import org.ecommerce.user.domain.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {}
