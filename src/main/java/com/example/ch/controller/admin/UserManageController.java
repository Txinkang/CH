package com.example.ch.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.Admin;
import com.example.ch.model.entity.User;
import com.example.ch.service.admin.UserManageService;

@RestController
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    private UserManageService userManageService;

    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam(value = "user_account", required = false) String userAccount, @RequestParam(value = "page_num", required = true) Integer pageNum, @RequestParam(value = "page_size", required = true) Integer pageSize){
        return userManageService.getUserInfo(userAccount, pageNum, pageSize);
    }

    @PatchMapping("/updateUser")
    public Result updateUser(@RequestBody User user){
        return userManageService.updateUser(user);
    }

    @DeleteMapping("/deleteUser")
    public Result deleteUser(@RequestParam(value = "user_id", required = true) String userId){
        return userManageService.deleteUser(userId);
    }


    //管理员
    @PostMapping("/createAdmin")
    public Result createAdmin(@RequestBody Admin admin){
        return userManageService.createAdmin(admin);
    }

    @GetMapping("/getAdminInfo")
    public Result getAdminInfo(@RequestParam(value = "admin_account", required = false) String adminAccount, @RequestParam(value = "page_num", required = true) Integer pageNum, @RequestParam(value = "page_size", required = true) Integer pageSize){
        return userManageService.getAdminInfo(adminAccount, pageNum, pageSize);
    }

    @PatchMapping("/updateAdmin")
    public Result updateAdmin(@RequestBody Admin admin){
        return userManageService.updateAdmin(admin);
    }

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestParam(value = "admin_id", required = true) String adminId){
        return userManageService.deleteAdmin(adminId);
    }
}
