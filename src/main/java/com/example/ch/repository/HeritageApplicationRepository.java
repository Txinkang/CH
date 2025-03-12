package com.example.ch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ch.model.entity.HeritageApplication;

public interface HeritageApplicationRepository extends JpaRepository<HeritageApplication, Long> {

    void deleteByProjectId(String projectId);

    HeritageApplication findByProjectId(String projectId);
    
}
