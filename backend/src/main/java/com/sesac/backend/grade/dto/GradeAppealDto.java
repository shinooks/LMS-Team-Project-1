package com.sesac.backend.grade.dto;

import com.sesac.backend.entity.AppealStatus;
import com.sesac.backend.entity.GradeAppeal;
import com.sesac.backend.entity.Grade;
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
    private UUID appealId;
    private String content;
    private AppealStatus status;
    private LocalDateTime createdAt;
    
    // 학생 정보
    private String studentName;
    private String studentNumber;
    
    // 과목 정보
    private String courseName;
    
    // 점수 정보
    private Integer requestedAssignScore;
    private Integer requestedMidtermScore;
    private Integer requestedFinalScore;

    public static GradeAppealDto from(GradeAppeal appeal) {
        GradeAppealDto dto = new GradeAppealDto();
        dto.setAppealId(appeal.getAppealId());
        dto.setContent(appeal.getContent());
        dto.setStatus(appeal.getStatus());
        dto.setCreatedAt(appeal.getCreatedAt());
        
        // Grade 엔티티에서 정보 가져오기
        Grade grade = appeal.getGrade();
        if (grade != null) {
            dto.setStudentName(grade.getStudentName());
            dto.setStudentNumber(grade.getStudentNumber());
            dto.setCourseName(grade.getCourseName());
            dto.setRequestedAssignScore(appeal.getRequestedAssignScore());
            dto.setRequestedMidtermScore(appeal.getRequestedMidtermScore());
            dto.setRequestedFinalScore(appeal.getRequestedFinalScore());
        }
        
        return dto;
    }
}