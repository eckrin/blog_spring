package com.example.spring_blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Member {
    private String id;
    private String pw;
    private String email;

    @Builder
    public Member(String id, String pw, String email) {
        this.id = id;
        this.pw = pw;
        this.email = email;
    }
}
