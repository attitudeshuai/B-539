package com.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.dto.LoginDTO;
import com.example.blog.dto.RegisterDTO;
import com.example.blog.dto.TokenVO;
import com.example.blog.entity.User;
import com.example.blog.mapper.UserMapper;
import com.example.blog.service.UserService;
import com.example.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public TokenVO login(LoginDTO loginDTO) {
        String password = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername())
                    .eq(User::getPassword, password);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }
        String token = jwtUtils.generateToken(user.getUsername());
        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken(token);
        tokenVO.setId(user.getId());
        tokenVO.setUsername(user.getUsername());
        tokenVO.setNickname(user.getNickname());
        tokenVO.setRole(user.getRole());
        return tokenVO;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setNickname(registerDTO.getNickname());
        user.setRole(0); // Default user role
        user.setCreateTime(LocalDateTime.now());
        this.save(user);
    }
}
