package com.example.ch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ch.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserAccount(String userAccount);

    User findByUserId(String userId);

}
