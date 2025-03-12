package com.example.ch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ch.model.entity.ForumPost;

public interface ForumPostRepository extends JpaRepository<ForumPost, String> {
    ForumPost findByPostTitle(String postTitle);

    ForumPost findByPostId(String postId);
}
