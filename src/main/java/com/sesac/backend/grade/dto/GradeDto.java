package com.sesac.backend.grade.dto;

import com.sesac.backend.entity.Grade;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 성적 조회 응답을 위한 DTO (Data Transfer Object)
 * 엔티티와 프레젠테이션 계층 사이의 데이터 전달 객체
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString// 객체의 문자열 표현을 위한 toString 메서드 자동 생성
@Builder
@AllArgsConstructor
public class GradeDto {
    private UUID gradeId;            // 성적 고유 ID
    private UUID scoreId;           // 추가
    private UUID courseOpeningId;   // 추가
    private UUID courseId;   // 추가
    private String courseName;       // 과목명
    private String courseCode;       // 과목 코드
    private String studentNumber;    // 학번
    private String studentName;      // 학생 이름
    private String semester;         // 학기 정보
    private int year;                // 년도
    private UUID professorId;    // 교수 ID
    private int assignmentScore;     // 과제 점수
    private int midtermScore;        // 중간고사 점수
    private int finalScore;          // 기말고사 점수
    private int totalScore;          // 총점

    private boolean visibility;
    private LocalDateTime visibilityStartDate;
    private LocalDateTime visibilityEndDate;
    /**
     * Grade 엔티티를 GradeDto로 변환하는 정적 팩토리 메서드
     * @param grade 변환할 Grade 엔티티
     * @return 변환된 GradeDto 객체
     */
    public static GradeDto from(Grade grade) {
        GradeDto dto = new GradeDto();
        dto.gradeId = grade.getGradeId();
        dto.scoreId = grade.getScore().getScoreId();              // 추가
        dto.courseOpeningId = grade.getCourseOpening().getOpeningId();   // 추가
        dto.courseName = grade.getCourseName();
        dto.courseCode = grade.getCourseCode();
        dto.studentNumber = grade.getStudentNumber(); // 학번
        dto.studentName = grade.getStudentName(); // 학생 이름
        dto.semester = grade.getSemester();  // 학기
        dto.assignmentScore = grade.getAssignScore(); // 과제 점수
        dto.midtermScore = grade.getMidtermScore(); // 중간고사 점수
        dto.finalScore = grade.getFinalScore(); // 기말고사 점수
        dto.totalScore = grade.getTotalScore(); // 총점
        dto.visibility = grade.isVisibility();
        dto.visibilityStartDate = grade.getVisibilityStartDate();
        dto.visibilityEndDate = grade.getVisibilityEndDate();
        dto.year = grade.getYear();
        dto.courseId = grade.getCourseId(); // 추가
        dto.professorId= grade.getProfessorId(); // 추가
        return dto;

    }








}