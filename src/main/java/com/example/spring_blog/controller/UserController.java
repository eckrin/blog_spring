package com.example.spring_blog.controller;

import com.example.spring_blog.dto.KakaoProfileDto;
import com.example.spring_blog.dto.OAuthTokenDto;
import com.example.spring_blog.model.User;
import com.example.spring_blog.repository.UserRepository;
import com.example.spring_blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

//인증이 되지 않은 사용자들은 /auth/이하 주소와 index, static 밑 파일만 허용

@Controller
public class UserController {

    @Autowired
    private UserService userService;

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


    @GetMapping("/auth/kakao/callback")
    public String getKakaoCallback(@RequestParam String code) {
        //액세스토큰 요청
        OAuthTokenDto oauthToken =  userService.requestKakaoAccessToken(code);
        //유저 프로필 요청
        KakaoProfileDto kakaoProfile = userService.requestKakaoUserProfile(oauthToken);
        //회원이면 로그인, 회원이 아니라면 자동 회원가입
        userService.setKakaoAutoLogin(kakaoProfile);

        return "redirect:/";
    }
}
