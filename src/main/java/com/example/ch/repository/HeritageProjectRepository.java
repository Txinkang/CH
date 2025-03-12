package com.example.ch.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ch.model.entity.HeritageProject;

public interface HeritageProjectRepository extends JpaRepository<HeritageProject, Long> {
    HeritageProject findByProjectTitle(String projectTitle);

    List<HeritageProject> findByUserId(String userId);

    Page<HeritageProject> findByStatus(int heritageProjectStatusPending, PageRequest pageRequest);

    HeritageProject findByProjectId(String projectId);

    HeritageProject findByProjectTitleAndStatusNot(String projectTitle, int heritageProjectStatusRejected);

    Page<HeritageProject> findByStatusNot(int heritageProjectStatusRejected, PageRequest pageRequest);

    void deleteByProjectId(String projectId);

    HeritageProject findByProjectTitleAndStatus(String projectTitle, int heritageProjectStatusApproved);

}
