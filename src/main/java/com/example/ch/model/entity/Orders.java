package com.example.ch.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Orders {
    @Id
    @Column(name = "order_id")
    private String orderId;
    @Basic
    @Column(name = "user_id")
    private String userId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "phone")
    private String phone;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "product_id")
    private String productId;
    @Basic
    @Column(name = "product_amount")
    private int productAmount;
    @Basic
    @Column(name = "single_price")
    private double singlePrice;
    @Basic
    @Column(name = "total_price")
    private double totalPrice;
    @Basic
    @Column(name = "status")
    private int status;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Basic
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public double getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(double singlePrice) {
        this.singlePrice = singlePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        Orders orders = (Orders) o;
        return productAmount == orders.productAmount && Double.compare(orders.singlePrice, singlePrice) == 0 && Double.compare(orders.totalPrice, totalPrice) == 0 && status == orders.status && Objects.equals(orderId, orders.orderId) && Objects.equals(userId, orders.userId) && Objects.equals(name, orders.name) && Objects.equals(phone, orders.phone) && Objects.equals(address, orders.address) && Objects.equals(productId, orders.productId) && Objects.equals(createdAt, orders.createdAt) && Objects.equals(updatedAt, orders.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, name, phone, address, productId, productAmount, singlePrice, totalPrice, status, createdAt, updatedAt);
    }
}
