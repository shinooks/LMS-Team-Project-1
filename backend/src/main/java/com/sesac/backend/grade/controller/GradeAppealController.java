package com.sesac.backend.grade.controller;

import com.sesac.backend.grade.dto.GradeAppealDto;
import com.sesac.backend.grade.dto.GradeAppealRequest;
import com.sesac.backend.grade.service.GradeAppealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
// ... 기존 import문

@Tag(name = "성적 이의신청", description = "성적 이의신청 관련 API")
@RestController
@RequestMapping("/grade-appeals")
@RequiredArgsConstructor
public class GradeAppealController {
    private final GradeAppealService appealService;

    @Operation(summary = "이의신청 생성", description = "새로운 성적 이의신청을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "이의신청 생성 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @PostMapping
    public ResponseEntity<Void> createAppeal(
            @Parameter(description = "이의신청 정보") @RequestBody GradeAppealRequest request) {
        appealService.createAppeal(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이의신청 승인", description = "이의신청을 승인하고 성적을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "이의신청 승인 성공")
    @ApiResponse(responseCode = "404", description = "이의신청을 찾을 수 없음")
    @PutMapping("/{appealId}/approve")
    public ResponseEntity<Void> approveAppeal(
            @Parameter(description = "이의신청 ID") @PathVariable UUID appealId) {
        appealService.processAppeal(appealId, true);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이의신청 거절", description = "이의신청을 거절합니다.")
    @ApiResponse(responseCode = "200", description = "이의신청 거절 성공")
    @ApiResponse(responseCode = "404", description = "이의신청을 찾을 수 없음")
    @PutMapping("/{appealId}/reject")
    public ResponseEntity<Void> rejectAppeal(
            @Parameter(description = "이의신청 ID") @PathVariable UUID appealId) {
        appealService.processAppeal(appealId, false);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "성적별 이의신청 조회", description = "특정 성적에 대한 모든 이의신청을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "성적을 찾을 수 없음")
    @GetMapping("/grade/{gradeId}")
    public ResponseEntity<List<GradeAppealDto>> getAppealsByGrade(
            @Parameter(description = "성적 ID") @PathVariable UUID gradeId) {
        return ResponseEntity.ok(appealService.getAppealsByGrade(gradeId));
    }
}