package com.sesac.backend.evaluation.assignment.controller;

import com.sesac.backend.evaluation.assignment.dto.request.AssignCreationRequest;
import com.sesac.backend.evaluation.assignment.dto.request.AssignScoreRequest;
import com.sesac.backend.evaluation.assignment.dto.request.AssignSubmissionRequest;
import com.sesac.backend.evaluation.assignment.dto.response.AssignResponse;
import com.sesac.backend.evaluation.assignment.dto.response.AssignSubmissionResponse;
import com.sesac.backend.evaluation.assignment.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dongjin 과제 controller http 요청을 받아 Assignment service 호출
 */
@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignments")
@RestController
@Tag(name = "과제 관리 API", description = "과제 생성, 제출, 채점, 조회 관련 엔드포인트")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "학생별 과제 조회", description = "학생아이디(studentId)")
    public ResponseEntity<List<AssignResponse>> getAllAssignment(@PathVariable UUID studentId) {
        try {
            return ResponseEntity.ok(assignmentService.getAllAssignments(studentId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 과제 조회 컨트롤러
     *
     * @param openingId
     * @param studentId
     * @return
     */
    @GetMapping("/{openingId}/{studentId}")
    @Operation(summary = "과제 조회", description = "수강신청아이디(openingId), 학생아이디(studentId)")
    public ResponseEntity<AssignResponse> getAssignment(@PathVariable UUID openingId,
        @PathVariable UUID studentId) {
        try {
            return ResponseEntity.ok(assignmentService.findAssign(openingId, studentId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 과제 생성 컨트롤러
     *
     * @param assignCreationRequest
     * @return
     */
    @PostMapping("/create")
    @Operation(summary = "과제 생성", description = "강의개설아이디(openingId), 학생아이디(studentId), 제목(title), 문제(description), 시작시간(openAt), 기한(deadline)")
    public ResponseEntity<AssignCreationRequest> createAssignment(
        @RequestBody AssignCreationRequest assignCreationRequest) {
        try {
            return ResponseEntity.ok(assignmentService.createAssignment(assignCreationRequest));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 과제 제출 컨트롤러
     *
     * @param request 과제 제출 요청
     * @return AssignSubmissionRequest
     */
    @PostMapping("/submit")
    @Operation(summary = "과제 제출", description = "학생아이디(studentId), 강의개설아이디(openingId), 업로드파일(file), 파일명(fileName)")
    public ResponseEntity<AssignSubmissionResponse> submitAssignment(
        AssignSubmissionRequest request) {
        try {
            return ResponseEntity.ok(assignmentService.submitAssignment(request));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 과제 채점 컨트롤러
     *
     * @param request
     * @return
     */
    @PostMapping("/score")
    @Operation(summary = "과제 채점", description = "수강신청아이디(openingId), 학생아이디(studentId)")
    public ResponseEntity<AssignScoreRequest> submitAssignmentScore(
        @RequestBody AssignScoreRequest request) {
        try {
            return ResponseEntity.ok(assignmentService.updateAssignScore(request));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
