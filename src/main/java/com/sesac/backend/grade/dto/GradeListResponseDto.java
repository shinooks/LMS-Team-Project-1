package com.sesac.backend.grade.dto;

import com.sesac.backend.entity.Grade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GradeListResponseDto {
    private int rank;               // 순위
    private String studentNumber;   // 학번
    private String studentName;     // 이름
    private String courseName;    // 강의명 추가
    private String courseCode;    // 강의코드 추가
    private String semester;      // 학기 추가
    private Integer year;         // 연도 추가
    private int assignScore;        // 과제
    private int midtermScore;       // 중간
    private int finalScore;         // 기말
    private int totalScore;         // 총점
    private String letterGrade;     // 학점(A+, A, B+ 등)

    public static GradeListResponseDto from(Grade grade, int rank) {
        GradeListResponseDto dto = new GradeListResponseDto();
        dto.rank = rank;
        dto.studentNumber = grade.getStudentNumber();
        dto.studentName = grade.getStudentName();

        dto.assignScore = grade.getAssignScore();
        dto.midtermScore = grade.getMidtermScore();
        dto.finalScore = grade.getFinalScore();
        dto.totalScore = grade.getTotalScore();

        dto.letterGrade = convertToLetterGrade(dto.totalScore);
        return dto;
    }

    private static String convertToLetterGrade(int totalScore) {
        if (totalScore >= 95) return "A+";
        else if (totalScore >= 90) return "A";
        else if (totalScore >= 85) return "B+";
        else if (totalScore >= 80) return "B";
        else if (totalScore >= 75) return "C+";
        else if (totalScore >= 70) return "C";
        else if (totalScore >= 65) return "D+";
        else if (totalScore >= 60) return "D";
        else return "F";
    }
}
