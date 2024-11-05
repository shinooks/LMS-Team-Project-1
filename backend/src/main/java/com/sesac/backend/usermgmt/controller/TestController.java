package com.sesac.backend.usermgmt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/")
    public String showHomePage() {
        return "index.html"; // 메인 페이지 템플릿 이름
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login.html"; // 로그인 뷰 템플릿 이름
    }

    @GetMapping("/join")
    public String showJoinPage() {
        return "join.html"; // 회원가입 뷰 템플릿 이름
    }
}
