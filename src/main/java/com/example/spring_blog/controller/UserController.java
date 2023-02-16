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

    @Value("${values.key}")
    private String secret_server_pwd;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

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

    //POST방식으로 key=value 데이터를 요청(카카오쪽으로)
    //->Retrofit2, OkHttp, RestTemplate 등 사용가능
    @GetMapping("/auth/kakao/callback")
    public String getKakaoCallback(@RequestParam String code) {

        //액세스토큰 요청
        RestTemplate rt = new RestTemplate();
        //헤더정보 추가를 위한 HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody에 key-value값 추가
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "f6c79072a0eed466ef0cead34c306912");
        params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //오브젝트를 담아 Http POST로 요청하기
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", //요청 url
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class //응답받을 타입
        );

        //json으로 오는 응답 받기
        //->Gson, Json Simple, ObjectMapper등 사용가능
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthTokenDto oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthTokenDto.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //유저 프로필 요청
        RestTemplate rt2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class //응답받을 타입
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfileDto kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfileDto.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //User 엔티티: username, password, email
        System.out.println("블로그서버 username: "+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
        System.out.println("블로그서버 email: "+kakaoProfile.getKakao_account().getEmail());
        System.out.println("블로그서버 password: "+ secret_server_pwd);

        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
                .email(kakaoProfile.getKakao_account().getEmail())
                .password(secret_server_pwd)
                .oauth("kakao")
                .build();

        User originUser = userService.findMember(kakaoUser.getUsername());

        //가입되지 않은 경우
        if(originUser.getUsername()==null) {
            System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다.");
            userService.join(kakaoUser);
        }
        //이미 가입된 경우 로그인처리
        else {
            System.out.println("기존 회원이므로 자동 로그인을 진행합니다. ");
            System.out.println();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), secret_server_pwd));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return "redirect:/";
    }
}
