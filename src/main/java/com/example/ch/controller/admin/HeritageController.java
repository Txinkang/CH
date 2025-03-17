package com.example.ch.controller.admin;

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
import com.example.ch.model.entity.HeritageNew;
import com.example.ch.model.entity.HeritageProject;
import com.example.ch.service.admin.HeritageService;

@RestController
@RequestMapping("/admin")
public class HeritageController {

    @Autowired
    private HeritageService heritageService;

    //======================================资讯======================================
    @PostMapping("/addNew")
    public Result addNew(@RequestBody HeritageNew heritageNew) {
        return heritageService.addNew(heritageNew);
    }

    @GetMapping("/getNew")
    public Result getNew(
        @RequestParam("new_title") String newTitle,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return heritageService.getNew(newTitle, pageNum, pageSize);
    }

    @PatchMapping("/updateNew")
    public Result updateNew(@RequestBody HeritageNew updateNew) {
        return heritageService.updateNew(updateNew);
    }

    @DeleteMapping("/deleteNew")
    public Result deleteNew(@RequestParam("new_id") String newId) {
        return heritageService.deleteNew(newId);
    }

    //======================================项目======================================
    @GetMapping("/getApplication")
    public Result getApplication(
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return heritageService.getApplication(pageNum, pageSize);
    }

    @PostMapping("/auditApplication")
    public Result auditApplication(@RequestBody HeritageApplication heritageApplication) {
        return heritageService.auditApplication(heritageApplication);
    }

    @GetMapping("/getProject")
    public Result getProject(
        @RequestParam("project_title") String projectTitle,
        @RequestParam("page_num") int pageNum,
        @RequestParam("page_size") int pageSize
    ) {
        return heritageService.getProject(projectTitle, pageNum, pageSize);
    }

    @PatchMapping("/updateProject")
    public Result updateProject(
        @RequestParam(value = "project_id", required = true) String projectId,
        @RequestParam(value = "project_title", required = false) String projectTitle,
        @RequestParam(value = "project_content", required = false) String projectContent,
        @RequestParam(value = "project_image", required = false) MultipartFile[] projectImage
    ) {
        return heritageService.updateProject(projectId, projectTitle, projectContent, projectImage);
    }

    @DeleteMapping("/deleteProject")
    public Result deleteProject(@RequestParam("project_id") String projectId) {
        return heritageService.deleteProject(projectId);
    }
}
