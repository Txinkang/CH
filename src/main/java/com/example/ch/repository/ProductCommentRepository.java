package com.example.ch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ch.model.entity.ProductComment;

public interface ProductCommentRepository extends JpaRepository<ProductComment, String> {
    Page<ProductComment> findByProductId(String productId, PageRequest pageRequest);

    void deleteByProductId(String productId);
}