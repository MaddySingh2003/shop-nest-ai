package com.ecommerce.ecommerce_backend.repository;

import com.ecommerce.ecommerce_backend.model.Address;
import com.ecommerce.ecommerce_backend.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserEmail(String email);
    Optional<Address> findByUserAndIsDefaultTrue(User user);

    List<Address> findByUser(User user);
}
