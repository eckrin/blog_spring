package com.example.spring_blog.service;

import com.example.spring_blog.model.Board;
import com.example.spring_blog.model.User;
import com.example.spring_blog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//스프링이 컴포넌트 스캔을 통해 빈에 등록
@Service
public class BoardService {

    @Autowired //DI
    private BoardRepository boardRepository;

    @Transactional
    public void write(Board board, User user) { //게시글, 작성자
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }

    public Page<Board> list(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
}
