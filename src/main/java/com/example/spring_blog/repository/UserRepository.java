package com.example.spring_blog.repository;

import com.example.spring_blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//DAO
//자동으로 bean등록이 된다.
@Repository //생략가능
public interface UserRepository extends JpaRepository<User, Integer> {
    //JpaRepository에 Entity클래스와 ID값이 제네릭으로 들어감
    //DB연동정보는 yml파일과 gradle을 보고 설정된다.

    //로그인 로직 추가 >> JPA 네이밍 전략 https://www.devkuma.com/docs/jpa/%EC%9E%90%EB%8F%99-%EC%83%9D%EC%84%B1-%EC%BF%BC%EB%A6%AC-%EB%A9%94%EC%86%8C%EB%93%9C%EC%9D%98-%EB%AA%85%EB%AA%85-%EA%B7%9C%EC%B9%99/
    //SELECT * FROM user WHERE username=? AND password=?;
    User findByUsernameAndPassword(String username, String password);

    //네이티브 쿼리 사용
//    @Query(value="select * from user where username=?1 and password=?2", nativeQuery = true)
//    User login(String username, String password);
}
