package com.example.ch.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ch.model.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, String> {

    Page<Orders> findByUserId(String userId, PageRequest pageRequest);

    
}
