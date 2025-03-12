package com.example.ch.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ch.model.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, String> {

    Page<Orders> findByOrderId(String orderId, PageRequest pageRequest);

    Page<Orders> findByUserIdAndProductIdAndOrderId(String userId, String productId, String orderId,
            PageRequest pageRequest);


    Page<Orders> findByUserId(String userId, PageRequest pageRequest);

    Page<Orders> findByUserIdAndProductId(String userId, String productId, PageRequest pageRequest);

    Page<Orders> findByUserIdAndOrderId(String userId, String orderId, PageRequest pageRequest);

    Page<Orders> findByProductIdAndOrderId(String productId, String orderId, PageRequest pageRequest);

    Page<Orders> findByProductId(String productId, PageRequest pageRequest);
}
