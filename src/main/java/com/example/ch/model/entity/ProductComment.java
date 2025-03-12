package com.example.ch.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "product_comment", schema = "ch", catalog = "")
public class ProductComment {
    @Id
    @Column(name = "product_comment_id")
    private String productCommentId;
    @Basic
    @Column(name = "user_id")
    private String userId;
    @Basic
    @Column(name = "product_id")
    private String productId;
    @Basic
    @Column(name = "product_score")
    private int productScore;
    @Basic
    @Column(name = "product_comment")
    private String productComment;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;

    public String getProductCommentId() {
        return productCommentId;
    }

    public void setProductCommentId(String productCommentId) {
        this.productCommentId = productCommentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getProductScore() {
        return productScore;
    }

    public void setProductScore(int productScore) {
        this.productScore = productScore;
    }

    public String getProductComment() {
        return productComment;
    }

    public void setProductComment(String productComment) {
        this.productComment = productComment;
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
        ProductComment that = (ProductComment) o;
        return productScore == that.productScore && Objects.equals(productCommentId, that.productCommentId) && Objects.equals(userId, that.userId) && Objects.equals(productId, that.productId) && Objects.equals(productComment, that.productComment) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCommentId, userId, productId, productScore, productComment, createdAt);
    }
}
