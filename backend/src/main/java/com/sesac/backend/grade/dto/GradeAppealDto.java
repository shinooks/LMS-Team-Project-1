package com.sesac.backend.grade.dto;

import com.sesac.backend.entity.AppealStatus;
import com.sesac.backend.entity.GradeAppeal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 성적 이의신청 정보를 전달하기 위한 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class GradeAppealDto {
    private UUID appealId;          // 이의신청 ID
    private UUID gradeId;           // 성적 ID
    private String studentName;     // 학생 이름
    private String studentNumber;   // 학번
    private String courseName;      // 과목명
    private String content;         // 이의신청 내용
    private AppealStatus status;    // 처리 상태 (PENDING, APPROVED, REJECTED)
    private LocalDateTime createdAt;// 신청일시

    // 현재 점수
    private int currentAssignScore;
    private int currentMidtermScore;
    private int currentFinalScore;

    // 요청 점수
    private int requestedAssignScore;
    private int requestedMidtermScore;
    private int requestedFinalScore;



    /**
     * GradeAppeal 엔티티를 GradeAppealDto로 변환
     * @param appeal 변환할 GradeAppeal 엔티티
     * @return 변환된 GradeAppealDto
     */
    public static GradeAppealDto from(GradeAppeal appeal) {
        GradeAppealDto dto = new GradeAppealDto();
        dto.appealId = appeal.getAppealId();
        dto.gradeId = appeal.getGrade().getGradeId();
        dto.studentName = appeal.getGrade().getStudentName();
        dto.studentNumber = appeal.getGrade().getStudentNumber();
        dto.courseName = appeal.getGrade().getCourseName();
        dto.content = appeal.getContent();
        dto.status = appeal.getStatus();
        dto.createdAt = appeal.getCreatedAt();
        dto.currentAssignScore = appeal.getGrade().getAssignScore();
        dto.currentMidtermScore = appeal.getGrade().getMidtermScore();
        dto.currentFinalScore = appeal.getGrade().getFinalScore();
        dto.requestedAssignScore = appeal.getRequestedAssignScore();
        dto.requestedMidtermScore = appeal.getRequestedMidtermScore();
        dto.requestedFinalScore = appeal.getRequestedFinalScore();
        return dto;
    }
}