package com.sesac.backend.enrollment.domain.tempClasses;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Classes { // 개설강의

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long classId; // test pk

    private String className; // 강의명
    private int maxEnrollments; //최대 수강인원
    private int currentEnrollments; //현재 수강인원

    private String day; //요일
    private String startTime; // 강의 시작 시간,  min: 09:00
    private String endTime; // 강의 종료 시간,  max: 18:00 1시간 단위로 교시 나누기
    private int credit; //학점, 1~3학점
}

//수강할 강의(장바구니) -> 강의명을 unique

//수강신청이 끝나고 수강하는 강의 ->
