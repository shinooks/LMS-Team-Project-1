package com.sesac.backend.grade.dto;

import com.sesac.backend.entity.Grade;
import lombok.*;

/**
 * 성적 조회 응답을 위한 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class GradeDto {
    private Long gradeId;
    private String courseName;        // 과목명 추가
    private String courseCode;        // 과목 코드 추가
    private String studentNumber;
    private String studentName;
    private String semester;        // 학기 추가
    private int assignmentScore;
    private int midScore;
    private int finalScore;
    private int totalScore;

    // 일반 생성자로 변환
    public static GradeDto from(Grade grade) {
        GradeDto dto = new GradeDto();
        dto.gradeId = grade.getGradeId();    // id -> gradeId로 변경
        dto.courseName = grade.getCourseName();
        dto.courseCode = grade.getCourseCode();
        dto.studentNumber = grade.getStudent().getStudentNumber();
        dto.studentName = grade.getStudent().getName();
        dto.semester = grade.getSemester();
        dto.assignmentScore = grade.getAssignmentScore();
        dto.midScore = grade.getMidScore();
        dto.finalScore = grade.getFinalScore();
        dto.totalScore = grade.getTotalScore();
        return dto;
    }
}