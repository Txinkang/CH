package com.example.ch.service.user;

import java.math.BigDecimal;

import com.example.ch.common.Response.Result;
import com.example.ch.model.entity.User;

public interface UserService {

    Result register(User user);

    Result login(User user);

    Result logout(String token);

    Result getUserInfo();

    Result updateUserInfo(User user);

    Result recharge(BigDecimal amount);

}
