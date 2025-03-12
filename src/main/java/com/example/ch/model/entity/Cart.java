package com.example.ch.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Cart {
    @Id
    @Column(name = "cart_id")
    private String cartId;
    @Basic
    @Column(name = "user_id")
    private String userId;
    @Basic
    @Column(name = "product_id")
    private String productId;
    @Basic
    @Column(name = "product_amount")
    private int productAmount;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Basic
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
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
        Cart cart = (Cart) o;
        return productAmount == cart.productAmount && Objects.equals(cartId, cart.cartId) && Objects.equals(userId, cart.userId) && Objects.equals(productId, cart.productId) && Objects.equals(createdAt, cart.createdAt) && Objects.equals(updatedAt, cart.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, userId, productId, productAmount, createdAt, updatedAt);
    }
}
