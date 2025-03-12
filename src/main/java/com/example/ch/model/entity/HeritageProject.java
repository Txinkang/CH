package com.example.ch.model.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "heritage_project", schema = "ch", catalog = "")
public class HeritageProject {
    @Id
    @Column(name = "project_id")
    private String projectId;
    @Basic
    @Column(name = "project_title")
    private String projectTitle;
    @Basic
    @Column(name = "project_content")
    private String projectContent;
    @Basic
    @Column(name = "project_image")
    private String projectImage;
    @Basic
    @Column(name = "project_material")
    private String projectMaterial;
    @Basic
    @Column(name = "user_id")
    private String userId;
    @Basic
    @Column(name = "status")
    private int status;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Basic
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public String getProjectId() {
        return projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectContent() {
        return projectContent;
    }

    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public String getProjectMaterial() {
        return projectMaterial;
    }

    public void setProjectMaterial(String projectMaterial) {
        this.projectMaterial = projectMaterial;
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
        HeritageProject that = (HeritageProject) o;
        return status == that.status && Objects.equals(projectId, that.projectId) && Objects.equals(projectTitle, that.projectTitle) && Objects.equals(projectContent, that.projectContent) && Objects.equals(projectImage, that.projectImage) && Objects.equals(projectMaterial, that.projectMaterial) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectTitle, projectContent, projectImage, projectMaterial, status, createdAt, updatedAt);
    }
}
