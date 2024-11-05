package com.sesac.backend.entity;

import com.sesac.backend.course.constant.Credit;
import jakarta.persistence.*;
        import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.UUID;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue
    private UUID courseId; // 강의ID
    // UUID : 전세계적으로 고유한 식별자.
    // 장점 : 중복될 확률이 낮음, 단점 : 문자열이 길어서 DB용량을 더 차지

    @Column(nullable = false, unique = true)
    private String courseCode; // 강의코드

    @Column(nullable = false)
    private String courseName; // 강의명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId", nullable = false)
    private Department department; // 학과ID

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)  // Enum 값을 숫자로 저장 (1,2,3)
    private Credit credits; // Credit enum 타입으로 선언

    private String description; // 설명

}
