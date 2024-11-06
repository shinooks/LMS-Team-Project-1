package com.sesac.backend.usermgmt.domain;

import com.sesac.backend.usermgmt.dto.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    // PK로 사용자 내부 식별 ID 생성 (랜덤)
    private UUID userUUID;

    @Column(nullable = false, unique = true)
    private String userNumber;

    // 패스워드 찾기 용 이메일,
    @Column(unique = true)
    private String email;

    // 사용자 패스워드 (해쉬 처리-> 항상 값 존재)
    @Column(nullable = false)
    private String password;

    // 마지막 로그인 타임 스탬프
    private Date lastLogin;

    // 생성일시, 시스템이 생성, 수정 불가
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    // 수정일시, 시스템이 업데이트
    @UpdateTimestamp
    private Date updatedAt;


    // 사용자 유형 (학생, 교수, 교직원)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    // DB로부터 사용자의 권한을 입력 받아 리스트로 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userType.toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


//    @Enumerated(EnumType.STRING)
//    private SocialProvider socialProvider; // 소셜로그인제공자
//    private String socialId; // 소셜ID



//    public enum SocialProvider {
//        GENERAL, GOOGLE, KAKAO, NAVER
//    }
}
