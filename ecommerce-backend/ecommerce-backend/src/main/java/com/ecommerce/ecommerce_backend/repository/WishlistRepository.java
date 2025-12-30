package com.ecommerce.ecommerce_backend.repository;

import com.ecommerce.ecommerce_backend.model.Wishlist;
import com.ecommerce.ecommerce_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUser(User user);
}
