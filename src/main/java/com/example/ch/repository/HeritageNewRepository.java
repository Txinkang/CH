package com.example.ch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ch.model.entity.HeritageNew;


public interface HeritageNewRepository extends JpaRepository<HeritageNew, String> {

    HeritageNew findByNewTitle(String newTitle);

    HeritageNew findByNewId(String newId);

}
