package com.example.ch.service.impl.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ch.service.admin.AdminService;
import com.example.ch.utils.JwtUtil;
import com.example.ch.utils.LogUtil;
import com.example.ch.common.Response.Result;
import com.example.ch.common.Response.ResultCode;
import com.example.ch.constant.MagicMathConstData;
import com.example.ch.constant.RedisConstData;
import com.example.ch.model.entity.Admin;
import com.example.ch.repository.AdminRepository;
import com.example.ch.common.Redis.RedisService;

@Service
public class AdminServiceimpl implements AdminService{
    @Autowired
    private AdminRepository adminRepository;


    @Autowired
    private RedisService redisService;
    
    private static final LogUtil logUtil = LogUtil.getLogger(AdminServiceimpl.class);

    @Override
    public Result login(Admin admin) {
        try {
            // 验证参数
            if (admin == null) {
                return new Result(ResultCode.R_ParamError);
            }
            if (admin.getAdminAccount() == null || admin.getAdminPassword() == null) {
                return new Result(ResultCode.R_ParamError);
            }
            // 验证管理员
            Admin adminQuery = adminRepository.findByAdminAccount(admin.getAdminAccount());
            if (adminQuery == null) {
                return new Result(ResultCode.R_UserNotFound);
            }
            // 验证密码
            if (!adminQuery.getAdminPassword().equals(admin.getAdminPassword())) {
                return new Result(ResultCode.R_PasswordError);
            }
            //存入信息生成token
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", adminQuery.getAdminId());
            String token = JwtUtil.genToken(userMap);
            boolean redisSet = redisService.set(RedisConstData.USER_LOGIN_TOKEN + adminQuery.getAdminId(), token, MagicMathConstData.REDIS_VERIFY_TOKEN_TIMEOUT, TimeUnit.HOURS);
            if (!redisSet) {
            return new Result(ResultCode.R_Fail);
            }
            return new Result(ResultCode.R_Ok, token);
        } catch (Exception e) {
            logUtil.error("admin login error", e);
            return new Result(ResultCode.R_Error);
        }
    }

    @Override
    public Result logout(String token) {
        try {
            Map<String, Object> userMap = JwtUtil.parseToken(token);
            //为null大概就是过期了，直接回ok就行
            if (userMap == null) {
                return new Result(ResultCode.R_Ok);
            }
            boolean redisDelete = redisService.delete(RedisConstData.USER_LOGIN_TOKEN + userMap.get("id"));
            return new Result(redisDelete ? ResultCode.R_Ok : ResultCode.R_UpdateDbFailed);
        } catch (Exception e) {
            logUtil.error("登出失败", e);
            return new Result(ResultCode.R_UpdateDbFailed);
        }
    }

}


