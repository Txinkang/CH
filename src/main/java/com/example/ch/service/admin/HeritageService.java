package com.example.ch.service.admin;

import org.springframework.web.multipart.MultipartFile;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.HeritageApplication;
import com.example.ch.model.entity.HeritageNew;
import com.example.ch.model.entity.HeritageProject;

public interface HeritageService {

    Result addNew(HeritageNew heritageNew);

    Result getNew(String newTitle, int pageNum, int pageSize);

    Result updateNew(HeritageNew updateNew);

    Result deleteNew(String newId);

    Result getApplication(int pageNum, int pageSize);

    Result auditApplication(HeritageApplication heritageApplication);

    Result getProject(String projectTitle, int pageNum, int pageSize);

    Result updateProject(String projectId, String projectTitle, String projectContent, MultipartFile[] projectImage);

    Result deleteProject(String projectId);
}
