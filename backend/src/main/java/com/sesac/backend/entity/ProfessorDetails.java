package com.sesac.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorDetails {

    @Id
    @GeneratedValue
    private UUID professorDetailsId; // 교수상세정보ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professorId", nullable = false)
    private Professor professor; // 교수ID

    private String officeLocation; // 연구실 위치
    private String officeHours; // 상담시간
    private String researchAreas; // 연구분야
    private String profileImageUrl; // 프로필이미지URL
}
