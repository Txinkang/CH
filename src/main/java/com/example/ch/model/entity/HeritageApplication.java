package com.example.ch.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "heritage_application", schema = "ch", catalog = "")
public class HeritageApplication {
    @Id
    @Column(name = "application_id")
    private String applicationId;
    @Basic
    @Column(name = "project_id")
    private String projectId;
    @Basic
    @Column(name = "status")
    private int status;
    @Basic
    @Column(name = "audit_response")
    private String auditResponse;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Basic
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuditResponse() {
        return auditResponse;
    }

    public void setAuditResponse(String auditResponse) {
        this.auditResponse = auditResponse;
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
        HeritageApplication that = (HeritageApplication) o;
        return projectId == that.projectId && status == that.status && Objects.equals(applicationId, that.applicationId) && Objects.equals(auditResponse, that.auditResponse) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, projectId, status, auditResponse, createdAt, updatedAt);
    }
}
