package org.ecommerce.user.infrastructure.repository.jpa;

import org.ecommerce.user.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {}
