package com.sesac.backend.grade.controller;

import com.sesac.backend.grade.domain.Grade;
import com.sesac.backend.grade.dto.ErrorDto;
import com.sesac.backend.grade.dto.requset.GradeUpdateRequestDto;
import com.sesac.backend.grade.dto.response.GradeResponseDto;
import com.sesac.backend.grade.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 성적 관리 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/grades")
@Tag(name = "성적 관리", description = "성적 조회 및 수정 API")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping("/{gradeId}")
    @Operation(summary = "성적 조회", description = "특정 성적 정보를 조회합니다.")
    public ResponseEntity<GradeResponseDto> getGrade(
            @PathVariable @Parameter(description = "성적 ID") Long gradeId) {
        return ResponseEntity.ok(gradeService.getGrade(gradeId));
    }

    @PutMapping("/{gradeId}")
    @Operation(summary = "성적 수정", description = "성적 정보를 수정합니다.")
    public ResponseEntity<GradeResponseDto> updateGrade(
            @PathVariable @Parameter(description = "성적 ID") Long gradeId,
            @Valid @RequestBody GradeUpdateRequestDto requestDto) {
        return ResponseEntity.ok(gradeService.updateGrade(gradeId, requestDto));
    }

    @GetMapping("/assignment/{assignmentId}")
    @Operation(summary = "과제별 성적 목록", description = "특정 과제의 모든 성적을 조회합니다.")
    public ResponseEntity<List<GradeResponseDto>> getGradesByAssignment(
            @PathVariable @Parameter(description = "과제 ID") Long assignmentId) {
        return ResponseEntity.ok(gradeService.getGradesByAssignment(assignmentId));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(e.getMessage()));
    }

    @GetMapping("/test")  // 테스트용 엔드포인트 추가
    public ResponseEntity<List<Grade>> test() {
        return ResponseEntity.ok(gradeRepository.findAll());
    }
}