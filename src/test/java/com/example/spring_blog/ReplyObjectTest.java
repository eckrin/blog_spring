package com.example.spring_blog;

import com.example.spring_blog.model.Reply;
import org.junit.jupiter.api.Test;

public class ReplyObjectTest {

    @Test
    public void toStringTest() {
        Reply reply = Reply.builder()
                .id(1)
                .user(null)
                .board(null)
                .content("hi")
                .build();

        System.out.println(reply); //오브젝트 출력시 toString 자동 호출
    }
}
