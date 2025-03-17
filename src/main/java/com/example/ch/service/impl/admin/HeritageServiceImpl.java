package com.example.ch.service.impl.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.ch.common.Response.PageResponse;
import com.example.ch.common.Response.Result;
import com.example.ch.common.Response.ResultCode;
import com.example.ch.constant.HeritageConstantData;
import com.example.ch.model.entity.HeritageApplication;
import com.example.ch.model.entity.HeritageNew;
import com.example.ch.model.entity.HeritageProject;
import com.example.ch.repository.HeritageApplicationRepository;
import com.example.ch.repository.HeritageNewRepository;
import com.example.ch.repository.HeritageProjectRepository;
import com.example.ch.service.admin.HeritageService;
import com.example.ch.utils.FileUtil;
import com.example.ch.utils.LogUtil;

@Service
public class HeritageServiceImpl implements HeritageService {
    private static final LogUtil logUtil = LogUtil.getLogger(HeritageServiceImpl.class);

    @Autowired
    private HeritageNewRepository heritageNewRepository;

    @Autowired
    private HeritageProjectRepository heritageProjectRepository;

    @Autowired
    private HeritageApplicationRepository heritageApplicationRepository;

    @Value("${uploadFilePath.heritagePicturesPath}")
    private String heritagePicturesPath;

    @Value("${uploadFilePath.heritageMaterialPath}")
    private String heritageMaterialPath;

    //=======================================资讯=======================================
    @Override
    public Result addNew(HeritageNew heritageNew) {
        try {
            // 验证参数
            if (Strings.isEmpty(heritageNew.getNewTitle()) || Strings.isEmpty(heritageNew.getNewContent())) {
                return new Result(ResultCode.R_ParamError);
            }
            HeritageNew queryNew = heritageNewRepository.findByNewTitle(heritageNew.getNewTitle());
            if (queryNew != null) {
                return new Result(ResultCode.R_FileExists);
            }
            heritageNew.setNewId(UUID.randomUUID().toString());
            heritageNew.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            heritageNew.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            heritageNewRepository.save(heritageNew);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("添加公告失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getNew(String newTitle, int pageNum, int pageSize) {
        try {
            PageResponse<HeritageNew> pageResponse = new PageResponse<>();
            if(newTitle != null && !newTitle.isEmpty()){
                List<HeritageNew> newList = new ArrayList<>();
                HeritageNew heritageNew = heritageNewRepository.findByNewTitle(newTitle);
                if(heritageNew != null){
                    newList.add(heritageNew);
                }
                pageResponse.setTotal_item(newList.size());
                pageResponse.setData(newList);
            } else {
                PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
                Page<HeritageNew> page = heritageNewRepository.findAll(pageRequest);
                pageResponse.setTotal_item(page.getTotalElements());
                pageResponse.setData(page.getContent());
            }
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取资讯失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result updateNew(HeritageNew updateNew) {
        try {
            // 验证参数
            if (Strings.isEmpty(updateNew.getNewId())) {
                return new Result(ResultCode.R_ParamError);
            }
            HeritageNew heritageNew = heritageNewRepository.findByNewId(updateNew.getNewId());
            if (heritageNew == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            if (!Strings.isEmpty(updateNew.getNewTitle())) {
                HeritageNew queryNewTitle = heritageNewRepository.findByNewTitle(updateNew.getNewTitle());
                if (queryNewTitle != null && !queryNewTitle.getNewId().equals(updateNew.getNewId())) {
                    return new Result(ResultCode.R_FileExists);
                }
                heritageNew.setNewTitle(updateNew.getNewTitle());
            }
            if (!Strings.isEmpty(updateNew.getNewContent())) {
                heritageNew.setNewContent(updateNew.getNewContent());
            }
            heritageNew.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            heritageNewRepository.save(heritageNew);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("更新资讯失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    @Transactional
    public Result deleteNew(String newId) {
        try {
            // 验证参数
            if (Strings.isEmpty(newId)) {
                return new Result(ResultCode.R_ParamError);
            }
            HeritageNew heritageNew = heritageNewRepository.findByNewId(newId);
            if (heritageNew == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            heritageNewRepository.delete(heritageNew);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("删除资讯失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    //======================================项目======================================
    @Override
    public Result getApplication(int pageNum, int pageSize) {
        try {
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
            Page<HeritageProject> page = heritageProjectRepository.findByStatus(HeritageConstantData.HERITAGE_PROJECT_STATUS_PENDING, pageRequest);
            return new Result(ResultCode.R_Ok, page.getContent());
        } catch (Exception e) {
            logUtil.error("获取项目申请失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result auditApplication(HeritageApplication heritageApplication) {
        try {
            // 验证参数
            if (Strings.isEmpty(heritageApplication.getProjectId())) {
                return new Result(ResultCode.R_ParamError);
            }
            // 先保存项目审核状态
            HeritageProject heritageProject = heritageProjectRepository.findByProjectId(heritageApplication.getProjectId());
            if (heritageProject == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            heritageProject.setStatus(heritageApplication.getStatus());
            heritageProjectRepository.save(heritageProject);
            // 再保存申请表
            HeritageApplication queryApplication = heritageApplicationRepository.findByProjectId(heritageApplication.getProjectId());
            if(queryApplication != null){
                queryApplication.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                if(heritageApplication.getStatus() != HeritageConstantData.HERITAGE_PROJECT_STATUS_REJECTED){
                    queryApplication.setAuditResponse(null);
                }else{
                    queryApplication.setAuditResponse(heritageApplication.getAuditResponse());
                }
                heritageApplicationRepository.save(queryApplication);
            }else{
                heritageApplication.setApplicationId(UUID.randomUUID().toString());
                heritageApplication.setProjectId(heritageApplication.getProjectId());
                heritageApplication.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                heritageApplication.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                if(heritageApplication.getStatus() != HeritageConstantData.HERITAGE_PROJECT_STATUS_REJECTED){
                    heritageApplication.setAuditResponse(null);
                }
                heritageApplicationRepository.save(heritageApplication);
            }
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("审核项目申请失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getProject(String projectTitle, int pageNum, int pageSize) {
        try {
            PageResponse<HeritageProject> pageResponse = new PageResponse<>();
            if(projectTitle != null && !projectTitle.isEmpty()){
                List<HeritageProject> projectList = new ArrayList<>();
                HeritageProject heritageProject = heritageProjectRepository.findByProjectTitleAndStatusNot(projectTitle, HeritageConstantData.HERITAGE_PROJECT_STATUS_PENDING);
                if(heritageProject != null){
                    projectList.add(heritageProject);
                }
                pageResponse.setTotal_item(projectList.size());
                pageResponse.setData(projectList);
            } else {
                PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
                Page<HeritageProject> page = heritageProjectRepository.findByStatusNot(HeritageConstantData.HERITAGE_PROJECT_STATUS_PENDING, pageRequest);
                pageResponse.setTotal_item(page.getTotalElements());
                pageResponse.setData(page.getContent());
            }
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取项目失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result updateProject(String projectId, String projectTitle, String projectContent, MultipartFile[] projectImage) {
        try {
            // 验证参数
            if (Strings.isEmpty(projectId)) {
                return new Result(ResultCode.R_ParamError);
            }
            HeritageProject queryProject = heritageProjectRepository.findByProjectId(projectId);
            if (queryProject == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            if (!Strings.isEmpty(projectTitle)) {
                HeritageProject queryProjectTitle = heritageProjectRepository.findByProjectTitle(projectTitle);
                if (queryProjectTitle != null && !queryProjectTitle.getProjectId().equals(projectId)) {
                    return new Result(ResultCode.R_FileExists);
                }
                queryProject.setProjectTitle(projectTitle);
            }
            if (!Strings.isEmpty(projectContent)) {
                queryProject.setProjectContent(projectContent);
            }
            if (projectImage != null && projectImage.length > 0) {
                // 保存新图片
                StringBuilder imageNames = new StringBuilder();
                for (MultipartFile image : projectImage) {
                    String uniqueFileName = FileUtil.saveFile(image, heritagePicturesPath);
                    if (uniqueFileName == null) {
                        return new Result(ResultCode.R_SaveFileError);
                    }
                    imageNames.append(uniqueFileName).append("|");
                }
                String finalImageNames = imageNames.toString();
                // 删除旧图片
                String oldImageNames = queryProject.getProjectImage();
                if (oldImageNames != null && !oldImageNames.isEmpty()) {
                    FileUtil.deleteFiles(oldImageNames, heritagePicturesPath);
                }
                queryProject.setProjectImage(finalImageNames);
            }
            heritageProjectRepository.save(queryProject);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("更新项目失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result deleteProject(String projectId) {
        try {
            // 验证参数
            if (Strings.isEmpty(projectId)) {
                return new Result(ResultCode.R_ParamError);
            }
            HeritageProject heritageProject = heritageProjectRepository.findByProjectId(projectId);
            if (heritageProject == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            // 删除图片
            String projectImage = heritageProject.getProjectImage();
            if (projectImage != null && !projectImage.isEmpty()) {
                FileUtil.deleteFiles(projectImage, heritagePicturesPath);
            }
            // 删除材料
            String projectMaterial = heritageProject.getProjectMaterial();
            if (projectMaterial != null && !projectMaterial.isEmpty()) {
                FileUtil.deleteFile(projectMaterial, heritageMaterialPath);
            }
            heritageProjectRepository.delete(heritageProject);
            heritageApplicationRepository.deleteByProjectId(projectId);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("删除项目失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }



}
