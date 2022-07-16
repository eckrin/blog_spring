package com.example.spring_blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment
    private int id;

    @Column(nullable = false, length=100)
    private String title;

    @Lob //대용량 데이터 관리
    private String content; //섬머노트 라이브러리 <html>태그 섞여서 디자인 >> 용량 커짐

    @ColumnDefault("0")
    private int count; //조회수

    @ManyToOne(fetch = FetchType.EAGER) //즉시로딩
    @JoinColumn(name="userId")
    private User user; //DB는 오브젝트를 저장할 수 없으므로 FK 사용

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER) // 연관관계의 주인이 아니다.
//    @JoinColumn(name="replyId") >> fk는 필요가 없다. DB는 여러 값을 가질 수 없으므로 fk는 reply가 갖고있으면 됨
    private List<Reply> reply;

    @CreationTimestamp //데이터가 insert/update시 자동으로 값 배정
    private Timestamp createDate;

}
