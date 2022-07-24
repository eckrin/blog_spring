package com.example.spring_blog.service;

import com.example.spring_blog.config.auth.PrincipalDetail;
import com.example.spring_blog.model.Board;
import com.example.spring_blog.model.User;
import com.example.spring_blog.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//스프링이 컴포넌트 스캔을 통해 빈에 등록
@Service
@Slf4j
public class BoardService {

    @Autowired //DI
    private BoardRepository boardRepository;

    @Transactional
    public void write(Board board, User user) { //게시글, 작성자
        board.setCount(0);
        board.setUser(user);
        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Page<Board> list(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board detail(int id) {
        return boardRepository.findById(id)
                .orElseThrow(()-> {
                    return new IllegalArgumentException("글 상세보기 실패: 아이디 찾을 수 없음");
                });
    }

    @Transactional
    public void delete(int id) {
        log.info("{}", id);
        boardRepository.deleteById(id);
    }

    @Transactional
    public void edit(int id, Board requestBoard) {
        Board board = boardRepository.findById(id)
                .orElseThrow(()-> {
                    return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없음");
                }); //영속화 완료
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
//        boardRepository.save(board); //더티체킹으로 필요없나봄
        //해당 함수 종료시 트랜잭션 종료, 더티체킹
    }
}
