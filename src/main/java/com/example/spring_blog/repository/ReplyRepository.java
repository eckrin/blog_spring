package com.example.spring_blog.repository;

import com.example.spring_blog.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    @Modifying
    @Query(value="insert into reply(userId, boardId, content, createDate) values(?1,?2,?3,now())", nativeQuery = true)
    //네이티브 쿼리에 있는 ?들에 파라미터들이 순서대로 들어간다.
    int mySave(int userId, int boardId, String content); //리턴값: 업데이터된 행의 개수를 리턴

}
