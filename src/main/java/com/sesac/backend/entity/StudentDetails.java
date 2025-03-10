package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDetails {

    @Id
    @GeneratedValue
    private UUID studentDetailsId; // 학생상세정보ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student; // 학생ID

    private String address; // 주소
    private String phoneNumber; // 전화번호
    private LocalDate birthdate; // 생년월일

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별

    private String profileImageUrl; // 프로필이미지URL

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
