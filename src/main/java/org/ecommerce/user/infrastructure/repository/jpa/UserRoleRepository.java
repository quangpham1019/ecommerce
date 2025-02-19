package org.ecommerce.user.infrastructure.repository.jpa;

import org.ecommerce.user.domain.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser_Id(Long user_id);
    Optional<UserRole> findByUser_IdAndRole_Id(Long user_id, Long role_id);
}
