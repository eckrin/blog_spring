package com.example.spring_blog.service;

import com.example.spring_blog.model.User;
import com.example.spring_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//스프링이 컴포넌트 스캔을 통해 빈에 등록
@Service
public class UserService {

    @Autowired //DI
    private UserRepository userRepository;

    @Transactional
    public void join(User user) {
        userRepository.save(user);
    }

    @Transactional(readOnly = true) //트랜잭션의 정합성 유지
    public User login(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }
}
