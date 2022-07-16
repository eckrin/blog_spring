package com.example.spring_blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity //해당 클래스를 읽어서 mysql에 테이블 생성
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //빌더 패턴
//@DynamicInsert //insert시 null이 아닌 컬럼들만 포함
public class User {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //numbering전략: DB의 전략 따라가기
    private int id; //sequence, auto_increment

    @Column(nullable=false, length = 30)
    private String username; //아이디

    @Column(nullable = false, length = 100) //해시를 이용하여 암호화
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

//    @ColumnDefault("user")
    @Enumerated(EnumType.STRING)
    private RoleType role; //Enum을 쓰는게 좋다. 도메인(범위) 설정 가능

    @CreationTimestamp // 시간이 자동으로 입력
    private Timestamp createDate;
}
