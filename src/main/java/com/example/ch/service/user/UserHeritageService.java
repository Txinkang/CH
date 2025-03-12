package com.example.ch.service.user;

import org.springframework.web.multipart.MultipartFile;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.HeritageApplication;
import com.example.ch.model.entity.HeritageProject;

public interface UserHeritageService {

    Result heritageApplication(String projectTitle, String projectContent, MultipartFile[] projectImage,
            MultipartFile projectMaterial);

    Result getSelfProject();

    Result updateProject(String projectId, String projectTitle, String projectContent, MultipartFile[] projectImage,
            MultipartFile projectMaterial);

    Result getFailReason(String projectId);

    Result retryApplication(String projectId);

    Result deleteProject(String projectId);

    Result getProject(String projectTitle, int pageNum, int pageSize);

    Result getProjectDetails(String projectId);

    Result getNew(String newTitle, int pageNum, int pageSize);
}
