package com.example.ch.service.impl.user;

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
import com.example.ch.common.userCommon.ThreadLocalUtil;
import com.example.ch.constant.HeritageConstantData;
import com.example.ch.model.entity.HeritageApplication;
import com.example.ch.model.entity.HeritageNew;
import com.example.ch.model.entity.HeritageProject;
import com.example.ch.repository.HeritageApplicationRepository;
import com.example.ch.repository.HeritageNewRepository;
import com.example.ch.repository.HeritageProjectRepository;
import com.example.ch.service.user.UserHeritageService;
import com.example.ch.utils.FileUtil;
import com.example.ch.utils.LogUtil;

@Service
public class UserHeritageServiceImpl implements UserHeritageService {
    private static final LogUtil logUtil = LogUtil.getLogger(UserHeritageServiceImpl.class);

    @Autowired
    private HeritageProjectRepository heritageProjectRepository;

    @Autowired
    private HeritageApplicationRepository heritageApplicationRepository;

    @Autowired
    private HeritageNewRepository heritageNewRepository;
    
    @Value("${uploadFilePath.heritagePicturesPath}")
    private String heritagePicturesPath;

    @Value("${uploadFilePath.heritageMaterialPath}")
    private String heritageMaterialPath;

    @Override
    public Result heritageApplication(String projectTitle, String projectContent, MultipartFile[] projectImage,
            MultipartFile projectMaterial) {
        try {
            String userId = ThreadLocalUtil.getUserId();
            if (Strings.isEmpty(userId)) {
                return new Result(ResultCode.R_ParamError, "用户ID不能为空");
            }
            if (!Strings.isEmpty(projectTitle)) {
                HeritageProject heritageProjectTitle = heritageProjectRepository.findByProjectTitle(projectTitle);
                if (heritageProjectTitle != null) {
                    return new Result(ResultCode.R_ParamError, "该标题已存在");
                }
            }
            HeritageProject heritageProject = new HeritageProject();
            heritageProject.setProjectId(UUID.randomUUID().toString());
            heritageProject.setUserId(userId);
            heritageProject.setProjectTitle(projectTitle);
            heritageProject.setProjectContent(projectContent);
            // 保存图片
            StringBuilder imageNames = new StringBuilder();
            for (MultipartFile image : projectImage) {
                String uniqueFileName = FileUtil.saveFile(image, heritagePicturesPath);
                if (uniqueFileName == null) {
                    return new Result(ResultCode.R_SaveFileError);
                }
                imageNames.append(uniqueFileName).append("|");
            }
            String finalImageNames = imageNames.toString();
            heritageProject.setProjectImage(finalImageNames);
            // 保存材料
            String materialName = FileUtil.saveFile(projectMaterial, heritageMaterialPath);
            if (materialName == null) {
                return new Result(ResultCode.R_SaveFileError);
            }
            heritageProject.setProjectMaterial(materialName);
            heritageProject.setStatus(HeritageConstantData.HERITAGE_PROJECT_STATUS_PENDING);
            heritageProject.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            heritageProject.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            heritageProjectRepository.save(heritageProject);
            return new Result(ResultCode.R_Ok, heritageProject);
        } catch (Exception e) {
            logUtil.error("申请失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getSelfProject() {
        try {
            String userId = ThreadLocalUtil.getUserId();
            if (Strings.isEmpty(userId)) {
                return new Result(ResultCode.R_ParamError, "用户ID不能为空");
            }
            List<HeritageProject> heritageProjects = heritageProjectRepository.findByUserId(userId);
            return new Result(ResultCode.R_Ok, heritageProjects);
        } catch (Exception e) {
            logUtil.error("获取失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }
    
    @Override
    public Result updateProject(String projectId, String projectTitle, String projectContent, MultipartFile[] projectImage, MultipartFile projectMaterial) {
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
                if (queryProjectTitle != null) {
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
            if (projectMaterial != null) {
                // 保存新材料
                String materialName = FileUtil.saveFile(projectMaterial, heritageMaterialPath);
                if (materialName == null) {
                    return new Result(ResultCode.R_SaveFileError);
                }
                // 删除旧材料
                String oldMaterialName = queryProject.getProjectMaterial();
                if (oldMaterialName != null && !oldMaterialName.isEmpty()) {
                    FileUtil.deleteFile(oldMaterialName, heritageMaterialPath);
                }
                queryProject.setProjectMaterial(materialName);
            }
            heritageProjectRepository.save(queryProject);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("更新项目失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getFailReason(String projectId) {
        try {
            HeritageApplication heritageApplication = heritageApplicationRepository.findByProjectId(projectId);
            if (heritageApplication == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            return new Result(ResultCode.R_Ok, heritageApplication.getAuditResponse());
        } catch (Exception e) {
            logUtil.error("查看失败原因失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result retryApplication(String projectId) {
        try {
            HeritageApplication heritageApplication = heritageApplicationRepository.findByProjectId(projectId);
            if (heritageApplication == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            heritageApplication.setStatus(HeritageConstantData.HERITAGE_PROJECT_STATUS_PENDING);
            heritageApplication.setAuditResponse(null);
            heritageApplication.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            heritageApplicationRepository.save(heritageApplication);

            HeritageProject heritageProject = heritageProjectRepository.findByProjectId(projectId);
            heritageProject.setStatus(HeritageConstantData.HERITAGE_PROJECT_STATUS_PENDING);
            heritageProjectRepository.save(heritageProject);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("重试申请失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    @Transactional
    public Result deleteProject(String projectId) {
        try {
            HeritageProject heritageProject = heritageProjectRepository.findByProjectId(projectId);
            if (heritageProject == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            String materialName = heritageProject.getProjectMaterial();
            if (materialName != null && !materialName.isEmpty()) {
                FileUtil.deleteFile(materialName, heritageMaterialPath);
            }
            String imageNames = heritageProject.getProjectImage();
            if (imageNames != null && !imageNames.isEmpty()) {
                FileUtil.deleteFiles(imageNames, heritagePicturesPath);
            }
            heritageProjectRepository.delete(heritageProject);
            heritageApplicationRepository.deleteByProjectId(projectId);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("删除项目失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result getProject(String projectTitle, int pageNum, int pageSize) {
        try {
            PageResponse<HeritageProject> pageResponse = new PageResponse<>();
            if(projectTitle != null && !projectTitle.isEmpty()){
                List<HeritageProject> projectList = new ArrayList<>();
                HeritageProject heritageProject = heritageProjectRepository.findByProjectTitleAndStatus(projectTitle, HeritageConstantData.HERITAGE_PROJECT_STATUS_APPROVED);
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
    public Result getProjectDetails(String projectId) {
        try {
            HeritageProject heritageProject = heritageProjectRepository.findByProjectId(projectId);
            if (heritageProject == null) {
                return new Result(ResultCode.R_FileNotFound);
            }
            return new Result(ResultCode.R_Ok, heritageProject);
        } catch (Exception e) {
            logUtil.error("获取项目详情失败", e);
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

}
