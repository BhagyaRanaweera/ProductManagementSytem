package com.example.productorderingsystem.service.interf;

import com.example.productorderingsystem.dto.LoginRequest;
import com.example.productorderingsystem.dto.Response;
import com.example.productorderingsystem.dto.UserDto;
import com.example.productorderingsystem.entity.User;

public interface UserService {
    Response registerUser(UserDto registrationRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getLoginUser();
    Response getUserInfoAndOrderHistory();
}