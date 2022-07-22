package com.example.spring_blog.config.auth;

import com.example.spring_blog.model.User;
import com.example.spring_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //로그인 요청(username, password)를 가로챌때 username이 DB에 있는지 확인하여 리턴 (pw는 알아서 처리)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUsername(username)
                .orElseThrow(()-> {
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
                });
        return new PrincipalDetail(principal); //시큐리티 세션에 User정보 저장
    }
}
