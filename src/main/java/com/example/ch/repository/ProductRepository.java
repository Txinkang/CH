package com.example.ch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ch.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

    Product findByProductName(String productName);

    Product findByProductId(String productId);
    
}
