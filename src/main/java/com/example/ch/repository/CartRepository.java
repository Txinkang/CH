package com.example.ch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ch.model.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, String> {

    Cart findByCartId(String cartId);

    List<Cart> findByUserId(String userId);
    
}
