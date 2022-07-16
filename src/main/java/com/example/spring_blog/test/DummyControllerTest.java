package com.example.spring_blog.test;

import com.example.spring_blog.model.RoleType;
import com.example.spring_blog.model.User;
import com.example.spring_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
public class DummyControllerTest {

    @Autowired
    private UserRepository userRepository;

    @DeleteMapping("/dummy/user/{id}")
    public String deleteUser(@PathVariable int id) {
        try {
            userRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            return "삭제에 실패하였습니다(DB에 존재하지 않음)";
        }
        return "삭제되었습니다. id:"+id;
    }

    @Transactional
    @PutMapping("/dummy/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User requestUser) { //json데이터를 요청해서 java 객체로 변환
        System.out.println("id:"+id);
        System.out.println("password:"+requestUser.getPassword());
        System.out.println("email:"+requestUser.getEmail());

//        save함수는 id를 전달하지 않거나 전달했더라도 데이터가 없으면 insert,
//        id를 전달했는데 데이터가있으면 update

//        1. 그냥 save함수를 사용하여 업데이트 > null이 저장되는 문제 발생
//        requestUser.setId(id);
//        requestUser.setUsername("kim2");
//        userRepository.save(requestUser);

//        2. repo에서 user를 받아와서 업데이트
        User user = userRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("수정에 실패했습니다.");
        });
        user.setPassword(requestUser.getPassword());
        user.setEmail(requestUser.getEmail());
//        userRepository.save(user); //update. 주석처리해도 dirty checking

        return user;
    }

    @GetMapping("/dummy/users")
    public List<User> list() {
        return userRepository.findAll();
    }

    //한 페이지당 2건의 데이터 리턴받기 >> Page 사용 (/dummy/user?page=*)
    //Page를 이용하여 다양한 기능 사용가능
    @GetMapping("/dummy/user")
    public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> pagingUser = userRepository.findAll(pageable);

        List<User> users = pagingUser.getContent();
        return users;
    }

    @GetMapping("/dummy/user/{id}")
    public User detail(@PathVariable int id) {
        //findById로 받은 값이 null일 경우를 처리
        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다. id:"+id);
            }
        });
        return user;
    }

    @PostMapping("/dummy/join")
    //변수명을 동일하게 설정하면 @RequestParam 생략가능
   public String join(User user) {
        System.out.println("username:"+user.getUsername());
        System.out.println("password:"+user.getPassword());
        System.out.println("email:"+user.getEmail());
        System.out.println("id:"+user.getId());
        System.out.println("role:"+user.getRole());
        System.out.println("createDate:"+user.getCreateDate());

        user.setRole(RoleType.USER); //@Enumerated 응용
        userRepository.save(user); //insert시 사용

        return "회원가입 완료";
    }
}
