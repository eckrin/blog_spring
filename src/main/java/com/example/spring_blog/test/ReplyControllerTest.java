package com.example.spring_blog.test;

import com.example.spring_blog.model.Board;
import com.example.spring_blog.model.Reply;
import com.example.spring_blog.repository.BoardRepository;
import com.example.spring_blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReplyControllerTest {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @GetMapping("/test/board/{id}")
    public Board getBoard(@PathVariable int id) {
        return boardRepository.findById(id).get();
        //jackson이라는 라이브러리 작동하여 오브젝트를 json으로 리턴 (모델의 getter를 호출)
        //양방향 연관관계로 인한 무한참조 발생
        //@JsonIgnoreProperties 사용
    }

    @GetMapping("/test/reply")
    public List<Reply> getReply() {
        return replyRepository.findAll();
        //이렇게 직접 Reply로 다이렉트로 접근하면 순환참조 자동 예방
    }
}

