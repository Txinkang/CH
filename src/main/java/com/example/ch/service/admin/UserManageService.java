package com.example.ch.service.admin;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.Admin;
import com.example.ch.model.entity.User;

public interface UserManageService {
    Result getUserInfo(String userAccount, Integer pageNum, Integer pageSize);

    Result updateUser(User user);

    Result deleteUser(String userId);

    Result getAdminInfo(String adminAccount, Integer pageNum, Integer pageSize);

    Result updateAdmin(Admin admin);

    Result deleteAdmin(String adminId);

    Result createAdmin(Admin admin);
}
