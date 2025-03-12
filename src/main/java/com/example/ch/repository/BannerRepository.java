package com.example.ch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ch.model.entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, String> {

    Banner findByBannerName(String bannerName);

    Banner findByBannerId(String bannerId);
    
}
