package com.example.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blog.dto.LoginDTO;
import com.example.blog.dto.RegisterDTO;
import com.example.blog.dto.TokenVO;
import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import com.example.blog.utils.JwtUtils;
import com.example.blog.utils.Result;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result<TokenVO> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success("注册成功");
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token == null || !jwtUtils.validateToken(token)) {
            return Result.error(401, "未授权");
        }
        Claims claims = jwtUtils.getClaimsByToken(token);
        String username = claims.getSubject();
        User user = userService.query().eq("username", username).one();
        user.setPassword(null); // Hide password
        return Result.success(user);
    }

    @GetMapping("/page")
    public Result<Page<User>> getUserPage(@RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size) {
        Page<User> page = new Page<>(current, size);
        userService.page(page);
        page.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(page);
    }

    @DeleteMapping("/delete/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功");
    }
}
