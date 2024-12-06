package com.sesac.backend.usermgmt.config;


import com.sesac.backend.usermgmt.domain.UserData;
import com.sesac.backend.usermgmt.repository.UserRepository;
import com.sesac.backend.usermgmt.dto.UserType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitUser {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder encoder;;

    @PostConstruct
    public void init() {
        // 초기 계정 설정, admin 계정이 없다면 2024001001/admin/STAFF(=admin) 권한으로 생성
        if(!repo.existsByUserNumber("2024001001")) {
            UserData admin = UserData.builder()
                .userNumber("2024001001")
                .password(encoder.encode("admin"))
                .userType(UserType.valueOf("STAFF"))
                .build();
            repo.save(admin);
        }
    }
}