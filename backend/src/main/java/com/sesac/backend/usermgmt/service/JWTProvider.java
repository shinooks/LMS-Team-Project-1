package com.sesac.backend.usermgmt.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JWTProvider {
    // application.yaml에 정의된 키 값을 사용
    @Value("${my_value.key}")
    private String SECRET_KEY;

    // 인증된 사용자에게 토큰 발급, 토큰에 담을 정보는 비즈니스 로직에 맞게 구성
    public String GenerateToken(Authentication authentication) {
        String name = authentication.getName();
        String token = JWT.create()
            .withHeader(Map.of(
                "alg","HS256",
                "typ","JWT"))
            .withSubject(name)
            // 토큰 검증에 사용할 정보들
            .withIssuedAt(new Date(System.currentTimeMillis())) // 발급 시간
            .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 만료 시간
            .sign(Algorithm.HMAC256(SECRET_KEY)); // 토큰 서명
        return token;
    }

    // 토큰 검증 절차, 서명 -> 시간 순으로 유효 여부 판단
    public boolean VerifyToken(String token) {
        try {
            // 서명 검증, 이 단계에서 토큰이 서버에서 발급되었는지를 검증한다.
            DecodedJWT verifiedToken = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
            // 시간으로 만료 여부 판단, 유효한 토큰인지 확인한다.
            if(verifiedToken.getIssuedAt().after(new Date(System.currentTimeMillis()))
                ||verifiedToken.getExpiresAt().before(new Date(System.currentTimeMillis()))){
                System.out.println("현재 시간: " + new Date(System.currentTimeMillis()));
                System.out.println("시작 시간: "+ verifiedToken.getIssuedAt());
                System.out.println("만료 시간: "+ verifiedToken.getExpiresAt());
                throw new JWTVerificationException("Time Invalid token");
            }
        }catch(JWTVerificationException e) {
            System.out.println("잘못된 토큰 입니다."+e.getMessage());
            return false;
        }
        return true;
    }
    // 유효한 토큰에 대해 서버 접근을 인가.
    public Authentication GetAuthentication(String token) {
        String username = JWT.decode(token).getSubject();

        // 토큰의 사용자 이름을 이용해 인증 객체 생성하기
        return new UsernamePasswordAuthenticationToken(username, null);
    }
}