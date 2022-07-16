package com.example.spring_blog.test;

import org.springframework.web.bind.annotation.*;

@RestController //사용자 body에 view가 아닌 data로응답
public class HttpControllerTest {

    @GetMapping("/http/test")
    public String lombokTest() {
        Member m = Member.builder().pw("123").email("email").build();
        System.out.println(m.getPw());
        m.setPw("1234");
        System.out.println(m.getPw());
        return "lombok test 완료";
    }

    @GetMapping("/http/get") //select
    public String getTest(@RequestParam Member member) { //@RequestParam 생략가능 (기본값 required=false)
        return "get 요청 : " + member.getId()+", "+member.getPw()+", "+member.getEmail();
    }

    @PostMapping("/http/post") //insert
    public String postTest(@RequestBody Member member) { //@RequestBody 생략 불가능, Member로 받으려면 JSON과 같은 형태로 보내야 함
        return "post 요청 : " + member.getId()+", "+member.getPw()+", "+member.getEmail();
    }

    @PutMapping("/http/put") //update
    public String putTest(@RequestBody Member member) {
        return "post 요청 : " + member.getId()+", "+member.getPw()+", "+member.getEmail();
    }

    @DeleteMapping("/http/delete") //delete
    public String deleteTest(@RequestBody Member member) {
        return "post 요청 : " + member.getId()+", "+member.getPw()+", "+member.getEmail();
    }
}
