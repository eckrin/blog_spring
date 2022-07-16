package com.example.spring_blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerTest {

    @GetMapping("/temp/noJSP")
    public String tempJsp() {
        return "jsp.html";
    }
}
