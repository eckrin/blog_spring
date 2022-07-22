package com.example.spring_blog.controller.api;

import com.example.spring_blog.dto.ResponseDto;
import com.example.spring_blog.model.RoleType;
import com.example.spring_blog.model.User;
import com.example.spring_blog.service.BoardService;
import com.example.spring_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user) {
        System.out.println("UserApiController:save 호출됨");
        user.setRole(RoleType.USER); //자동주입이 안되는 필드 주입
        userService.join(user);//js로 받은 json을 그대로 전달

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    // without spring security
    // 이제는 스프링 시큐리티가 가로채게 함
//    @PostMapping("/api/user/login")
//    public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) {
//        System.out.println("UserApiController:login 호출됨");
//        User principal = userService.login(user); //principal(접근주체)
//
//        if (principal != null) {
//            session.setAttribute("principal", principal); //세션 생성
//        }
//
//        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//    }

}
