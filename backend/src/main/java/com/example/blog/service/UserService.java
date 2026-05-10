package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.dto.LoginDTO;
import com.example.blog.dto.RegisterDTO;
import com.example.blog.dto.TokenVO;
import com.example.blog.entity.User;

public interface UserService extends IService<User> {
    TokenVO login(LoginDTO loginDTO);
    void register(RegisterDTO registerDTO);
}
