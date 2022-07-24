package com.example.spring_blog.controller;

import com.example.spring_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//인증이 되지 않은 사용자들은 /auth/이하 주소와 index, static 밑 파일만 허용

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/auth/joinForm")
    public String joinForm() {
        return "/user/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "/user/updateForm";
    }
}
