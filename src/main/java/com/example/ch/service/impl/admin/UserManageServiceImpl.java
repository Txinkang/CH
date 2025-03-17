package com.example.ch.service.impl.admin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ch.common.Redis.RedisService;
import com.example.ch.common.Response.PageResponse;
import com.example.ch.common.Response.Result;
import com.example.ch.common.Response.ResultCode;
import com.example.ch.constant.RedisConstData;
import com.example.ch.model.entity.Admin;
import com.example.ch.model.entity.User;
import com.example.ch.repository.AdminRepository;
import com.example.ch.repository.UserRepository;
import com.example.ch.service.admin.UserManageService;
import com.example.ch.utils.LogUtil;

@Service
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    private static final LogUtil logUtil = LogUtil.getLogger(UserManageServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public Result getUserInfo(String userAccount, Integer pageNum, Integer pageSize) {
        try {
            PageResponse<User> pageResponse = new PageResponse<>();
            if(userAccount != null && !userAccount.isEmpty()){
                List<User> userList = new ArrayList<>();
                User user = userRepository.findByUserAccount(userAccount);
                if (user != null) {
                    userList.add(user);
                }
                pageResponse.setTotal_item(userList.size());
                pageResponse.setData(userList);
            } else {
                PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
                Page<User> page = userRepository.findAll(pageRequest);
                pageResponse.setTotal_item(page.getTotalElements());
                pageResponse.setData(page.getContent());
            }
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取用户信息失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result updateUser(User user) {
        try {
            User userInfo = userRepository.findByUserId(user.getUserId());
            if(userInfo != null){
                if(user.getUserAccount() != null && !user.getUserAccount().isEmpty()){
                    User queryUser = userRepository.findByUserAccount(user.getUserAccount());
                    if(queryUser != null && !queryUser.getUserAccount().equals(userInfo.getUserAccount())){
                        return new Result(ResultCode.R_UserAccountIsExist);
                    }
                    userInfo.setUserAccount(user.getUserAccount());
                }
                if(user.getUserPassword() != null && !user.getUserPassword().isEmpty()){
                    userInfo.setUserPassword(user.getUserPassword());
                }
                userInfo.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                userRepository.saveAndFlush(userInfo);
                boolean redisDelete = redisService.delete(RedisConstData.USER_LOGIN_TOKEN + userInfo.getUserId());
                return new Result(redisDelete ? ResultCode.R_Ok : ResultCode.R_UpdateDbFailed);
            }else{
                return new Result(ResultCode.R_UserNotFound);
            }
        } catch (Exception e) {
            logUtil.error("更新用户信息失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result deleteUser(String userId) {
        try {
            User userInfo = userRepository.findByUserId(userId);
            if(userInfo != null){
                userRepository.delete(userInfo);
                boolean redisDelete = redisService.delete(RedisConstData.USER_LOGIN_TOKEN + userInfo.getUserId());
                return new Result(redisDelete ? ResultCode.R_Ok : ResultCode.R_UpdateDbFailed);
            }else{
                return new Result(ResultCode.R_UserNotFound);
            }
        } catch (Exception e) {
            logUtil.error("删除用户失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }


    
    @Override
    public Result getAdminInfo(String adminAccount, Integer pageNum, Integer pageSize) {
        try {
            PageResponse<Admin> pageResponse = new PageResponse<>();
            if(adminAccount != null && !adminAccount.isEmpty()){
                List<Admin> adminList = new ArrayList<>();
                Admin admin = adminRepository.findByAdminAccount(adminAccount);
                if(admin != null){
                    adminList.add(admin);
                }
                pageResponse.setTotal_item(adminList.size());
                pageResponse.setData(adminList);
            } else {
                PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
                Page<Admin> page = adminRepository.findAll(pageRequest);
                pageResponse.setTotal_item(page.getTotalElements());
                pageResponse.setData(page.getContent());
            }
            return new Result(ResultCode.R_Ok, pageResponse);
        } catch (Exception e) {
            logUtil.error("获取管理员信息失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result updateAdmin(Admin admin) {
        try {
            Admin adminInfo = adminRepository.findByAdminId(admin.getAdminId());
            if(adminInfo != null){
                if(admin.getAdminAccount() != null && !admin.getAdminAccount().isEmpty()){
                    Admin queryAdmin = adminRepository.findByAdminAccount(admin.getAdminAccount());
                    if(queryAdmin != null && !queryAdmin.getAdminAccount().equals(adminInfo.getAdminAccount())){
                        return new Result(ResultCode.R_UserAccountIsExist);
                    }
                    adminInfo.setAdminAccount(admin.getAdminAccount());
                }
                if(admin.getAdminPassword() != null && !admin.getAdminPassword().isEmpty()){
                    adminInfo.setAdminPassword(admin.getAdminPassword());
                }
                adminInfo.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                adminRepository.saveAndFlush(adminInfo);
                boolean redisDelete = redisService.delete(RedisConstData.USER_LOGIN_TOKEN + adminInfo.getAdminId());
                return new Result(redisDelete ? ResultCode.R_Ok : ResultCode.R_UpdateDbFailed);
            }else{
                return new Result(ResultCode.R_UserNotFound);
            }
        } catch (Exception e) {
            logUtil.error("更新管理员信息失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result deleteAdmin(String adminId) {
        try {
            Admin adminInfo = adminRepository.findByAdminId(adminId);
            if(adminInfo != null){
                adminRepository.delete(adminInfo);
                boolean redisDelete = redisService.delete(RedisConstData.USER_LOGIN_TOKEN + adminInfo.getAdminId());
                return new Result(redisDelete ? ResultCode.R_Ok : ResultCode.R_UpdateDbFailed);
            }else{
                return new Result(ResultCode.R_UserNotFound);
            }
        } catch (Exception e) {
            logUtil.error("删除管理员失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

    @Override
    public Result createAdmin(Admin admin) {
        try {
            Admin adminInfo = adminRepository.findByAdminAccount(admin.getAdminAccount());
            if(adminInfo != null){
                return new Result(ResultCode.R_UserAccountIsExist);
            }
            admin.setAdminId(UUID.randomUUID().toString());
            admin.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            admin.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            adminRepository.save(admin);
            return new Result(ResultCode.R_Ok);
        } catch (Exception e) {
            logUtil.error("创建管理员失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }
}
