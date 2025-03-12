package com.example.ch.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "forum_post", schema = "ch", catalog = "")
public class ForumPost {
    @Id
    @Column(name = "post_id")
    private String postId;
    @Basic
    @Column(name = "post_title")
    private String postTitle;
    @Basic
    @Column(name = "post_content")
    private String postContent;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Basic
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
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
        ForumPost forumPost = (ForumPost) o;
        return Objects.equals(postId, forumPost.postId) && Objects.equals(postTitle, forumPost.postTitle) && Objects.equals(postContent, forumPost.postContent) && Objects.equals(createdAt, forumPost.createdAt) && Objects.equals(updatedAt, forumPost.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, postTitle, postContent, createdAt, updatedAt);
    }
}
