package com.sesac.backend.usermgmt.service;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Request의 Header에 Authorization 정보가 담겨 있다면 검증 진행.
        if(request.getHeader("Authorization")!=null && request.getHeader("Authorization").startsWith("Bearer ")) {
            String token = request.getHeader("Authorization").substring(7);
            System.out.println("토큰이 입력되었습니다.:"+token);
            if(jwtProvider.VerifyToken(token)) {

                // Security Context에 Authentication 객체를 등록(서버에서 인증된 상태로 만듦)
                SecurityContextHolder.getContext().setAuthentication(jwtProvider.GetAuthentication(token));

                System.out.println("토큰으로 인증 되었습니다.");
                return; // 인증되면 여기서 필터 체인 중단
            };
        }
        //  Authorization 정보가 없거나 유효하지 않다면 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
