package com.sesac.backend.grade.dto;

import com.sesac.backend.entity.Grade;
import com.sesac.backend.entity.Student;
import lombok.*;

/**
 * 성적 조회 응답을 위한 DTO (Data Transfer Object)
 * 엔티티와 프레젠테이션 계층 사이의 데이터 전달 객체
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString// 객체의 문자열 표현을 위한 toString 메서드 자동 생성
public class GradeDto {
    private Long gradeId;            // 성적 고유 ID
    private String courseName;       // 과목명
    private String courseCode;       // 과목 코드
    private String studentNumber;    // 학번
    private String studentName;      // 학생 이름
    private String semester;         // 학기 정보
    private int assignmentScore;     // 과제 점수
    private int midScore;           // 중간고사 점수
    private int finalScore;         // 기말고사 점수
    private int totalScore;         // 총점

    /**
     * Grade 엔티티를 GradeDto로 변환하는 정적 팩토리 메서드
     * @param grade 변환할 Grade 엔티티
     * @return 변환된 GradeDto 객체
     */
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