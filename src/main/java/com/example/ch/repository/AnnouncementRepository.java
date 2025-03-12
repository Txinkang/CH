package com.example.ch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ch.model.entity.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, String> {
    Announcement findByAnnouncementTitle(String announcementTitle);

    Announcement findByAnnouncementId(String announcementId);
}
