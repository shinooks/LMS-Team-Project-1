package com.sesac.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sesac.backend.course.constant.Credit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Table(name = "course")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    private Integer credits; // DB에는 숫자로 저장 (1,2,3)

    private String description; // 설명

    public static class CourseBuilder {
        private Credit creditsEnum; // builder용 임시 필드

        public CourseBuilder credits(Credit credits) {
            this.credits = credits.getValue();
            return this;
        }
    }

    // POST 요청 시 Enum -> Integer 변환
    public void setCredits(Credit credit) {
        this.credits = credit.getValue();
    }

    // GET 응답 시 Integer -> Enum 변환
    public Credit getCreditsEnum() {
        return Arrays.stream(Credit.values())
                .filter(c -> c.getValue() == this.credits)
                .findFirst()
                .orElse(null);
    }
}