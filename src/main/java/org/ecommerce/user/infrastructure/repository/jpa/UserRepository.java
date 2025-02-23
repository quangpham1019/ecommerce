package org.ecommerce.user.infrastructure.repository.jpa;

import org.ecommerce.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail_Address(String emailAddress);

    @Query("SELECT u.email FROM User u WHERE u.email IN :emails")
    List<String> findEmailsByEmails(@Param("emails") Set<String> emails);
}
