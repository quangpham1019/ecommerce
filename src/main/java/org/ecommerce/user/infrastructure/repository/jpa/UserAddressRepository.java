package org.ecommerce.user.infrastructure.repository.jpa;

import org.ecommerce.user.domain.model.Address;
import org.ecommerce.user.domain.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findAllByUser_Id(Long id);
}
