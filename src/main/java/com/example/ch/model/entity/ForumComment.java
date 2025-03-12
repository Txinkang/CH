package com.example.ch.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "forum_comment", schema = "ch", catalog = "")
public class ForumComment {
    @Id
    @Column(name = "comment_id")
    private String commentId;
    @Basic
    @Column(name = "post_id")
    private String postId;
    @Basic
    @Column(name = "user_id")
    private String userId;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumComment that = (ForumComment) o;
        return Objects.equals(commentId, that.commentId) && Objects.equals(postId, that.postId) && Objects.equals(userId, that.userId) && Objects.equals(content, that.content) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, postId, userId, content, createdAt);
    }
}
