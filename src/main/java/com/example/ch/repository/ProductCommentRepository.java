package com.example.ch.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ch.model.entity.ProductComment;

public interface ProductCommentRepository extends JpaRepository<ProductComment, String> {
    Page<ProductComment> findByProductId(String productId, Pageable pageable);
    List<ProductComment> findByProductId(String productId);
    void deleteByProductId(String productId);
    List<ProductComment> findByProductIdOrderByCreatedAtDesc(String productId);   
}
