package com.example.spring_blog.service;

import com.example.spring_blog.model.RoleType;
import com.example.spring_blog.model.User;
import com.example.spring_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//스프링이 컴포넌트 스캔을 통해 빈에 등록
@Service
public class UserService {

    @Autowired //DI
    private UserRepository userRepository;

    @Autowired //의존성 주입
    private BCryptPasswordEncoder encode;

    @Transactional
    public void join(User user) {
//        userRepository.findByUsername(user.getUsername()).orElseThrow(()-> {
//            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
//        });
        String rawPassword = user.getPassword();
        String encPassword = encode.encode(rawPassword); //pw 해시화
        user.setRole(RoleType.USER);
        user.setPassword(encPassword);
        userRepository.save(user);
    }

    @Transactional
    public void update(User user) {
        //수정시 영속성 컨텍스트 user 오브젝트를 영속화시키고, 영속화된 user 오브젝트 수정
        //더티 체킹을 이용하는것.
        User persistance = userRepository.findById(user.getId()).orElseThrow(()-> {
            return new IllegalArgumentException("회원 찾기 실패");
        });
        String rawPassword = user.getPassword();
        String encPassword = encode.encode(rawPassword);
        persistance.setPassword(encPassword);
        persistance.setEmail(user.getEmail());
        //update 함수 종료시 자동 트랜잭션 커밋
    }

//    @Transactional(readOnly = true) //트랜잭션의 정합성 유지
//    public User login(User user) {
//        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//    }
}
