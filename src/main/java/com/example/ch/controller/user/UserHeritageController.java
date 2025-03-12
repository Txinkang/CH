package com.example.ch.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.HeritageApplication;
import com.example.ch.model.entity.HeritageProject;
import com.example.ch.service.user.UserHeritageService;

@RestController
@RequestMapping("/user")
public class UserHeritageController {

    @Autowired
    private UserHeritageService heritageService;

    @PostMapping("/heritageApplication")
    public Result heritageApplication(
        @RequestParam(value = "project_title", required = true) String projectTitle,
        @RequestParam(value = "project_content", required = true) String projectContent,
        @RequestParam(value = "project_image", required = true) MultipartFile[] projectImage,
        @RequestParam(value = "project_material", required = true) MultipartFile projectMaterial
    ) {
        return heritageService.heritageApplication(projectTitle, projectContent, projectImage, projectMaterial);
    }


    @GetMapping("/getSelfProject")
    public Result getSelfProject() {
        return heritageService.getSelfProject();
    }

    @PatchMapping("/updateProject")
    public Result updateProject(
        @RequestParam(value = "project_id", required = true) String projectId,
        @RequestParam(value = "project_title", required = false) String projectTitle,
        @RequestParam(value = "project_content", required = false) String projectContent,
        @RequestParam(value = "project_image", required = false) MultipartFile[] projectImage,
        @RequestParam(value = "project_material", required = false) MultipartFile projectMaterial

    ) {
        return heritageService.updateProject(projectId, projectTitle, projectContent, projectImage, projectMaterial);
    }

    @GetMapping("/getFailReason")
    public Result getFailReason(@RequestParam(value = "project_id", required = true) String projectId) {
        return heritageService.getFailReason(projectId);
    }

    @GetMapping("/retryApplication")
    public Result retryApplication(@RequestParam(value = "project_id", required = true) String projectId) {
        return heritageService.retryApplication(projectId);
    }

    @DeleteMapping("/deleteProject")
    public Result deleteProject(@RequestParam("project_id") String projectId) {
        return heritageService.deleteProject(projectId);
    }

    @GetMapping("/getProject")
    public Result getProject(
        @RequestParam("project_title") String projectTitle,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return heritageService.getProject(projectTitle, pageNum, pageSize);
    }

    @GetMapping("/getProjectDetails")
    public Result getProjectDetails(
        @RequestParam("project_id") String projectId
    ) {
        return heritageService.getProjectDetails(projectId);
    }

    @GetMapping("/getNew")
    public Result getNew(
        @RequestParam(value = "new_title", required = false) String newTitle,
        @RequestParam(value = "page_num", required = true) int pageNum,
        @RequestParam(value = "page_size", required = true) int pageSize
    ) {
        return heritageService.getNew(newTitle, pageNum, pageSize);
    }

}
