package com.example.ch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ch.model.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    Admin findByAdminAccount(String adminAccount);

    Admin findByAdminId(String adminId);
    
}