package com.example.ch.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.Admin;
import com.example.ch.service.admin.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        return adminService.login(admin);
    }
    
    @GetMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String token){
        return adminService.logout(token);
    }
}
