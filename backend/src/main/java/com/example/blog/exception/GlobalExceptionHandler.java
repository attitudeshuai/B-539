package com.example.blog.exception;

import com.example.blog.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(500, e.getMessage());
    }

    @ExceptionHandler(org.springframework.web.bind.MissingPathVariableException.class)
    public Result<String> handleMissingPath(Exception e) {
        return Result.error(400, "路径参数缺失");
    }
}
