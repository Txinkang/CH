package com.example.ch.service.admin;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.Admin;

public interface AdminService {

    Result login(Admin admin);

    Result logout(String token);
    
}
