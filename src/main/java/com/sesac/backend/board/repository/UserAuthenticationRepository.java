package com.sesac.backend.board.repository;


import com.sesac.backend.board.constant.SocialProvider;
import com.sesac.backend.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, UUID> {
    // 이메일로 사용자 찾기
    Optional<UserAuthentication> findByEmail(String email);

    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);

    // 소셜ID와 제공자로 사용자 찾기
    Optional<UserAuthentication> findBySocialIdAndSocialProvider(String socialId, SocialProvider socialProvider);

    // 활성 사용자만 찾기
    Optional<UserAuthentication> findByEmailAndIsActiveTrue(String email);
}
