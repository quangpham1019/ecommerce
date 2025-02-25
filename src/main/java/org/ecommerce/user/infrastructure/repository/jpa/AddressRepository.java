package org.ecommerce.user.infrastructure.repository.jpa;

import org.ecommerce.user.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {}
