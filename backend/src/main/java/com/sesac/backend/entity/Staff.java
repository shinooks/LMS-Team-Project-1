package com.sesac.backend.entity;

import com.sesac.backend.usermgmt.domain.UserData;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {

    @Id
    @GeneratedValue
    private UUID staffId; // 교직원ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserData user; // 사용자ID

    @Column(nullable = false, unique = true)
    private String staffNumber; // 직원번호

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private String department; // 부서

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일시

    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일시
}
