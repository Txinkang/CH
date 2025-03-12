package com.example.ch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ch.model.entity.ForumComment;

public interface ForumCommentRepository extends JpaRepository<ForumComment, String> {

    void deleteByPostId(String postId);

    List<ForumComment> findByPostId(String postId);
    
}
