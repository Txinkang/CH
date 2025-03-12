package com.example.ch.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Banner {
    @Id
    @Column(name = "banner_id")
    private String bannerId;
    @Basic
    @Column(name = "banner_name")
    private String bannerName;
    @Basic
    @Column(name = "banner_image")
    private String bannerImage;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Basic
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banner banner = (Banner) o;
        return Objects.equals(bannerId, banner.bannerId) && Objects.equals(bannerName, banner.bannerName) && Objects.equals(bannerImage, banner.bannerImage) && Objects.equals(createdAt, banner.createdAt) && Objects.equals(updatedAt, banner.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bannerId, bannerName, bannerImage, createdAt, updatedAt);
    }
}
