package com.sesac.backend.evaluation.assignment.controller;

import com.sesac.backend.evaluation.assignment.dto.AssignCreationRequest;
import com.sesac.backend.evaluation.assignment.dto.AssignResponse;
import com.sesac.backend.evaluation.assignment.dto.AssignScoreRequest;
import com.sesac.backend.evaluation.assignment.dto.AssignSubmissionRequest;
import com.sesac.backend.evaluation.assignment.service.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * @param studentId
     * @param openingId
     * @param multipartFile
     * @param fileName
     * @return
     */
    @PostMapping("/submit")
    @Operation(summary = "과제 제출", description = "학생아이디(studentId), 강의개설아이디(openingId), 업로드파일(file), 파일명(fileName)")
    public ResponseEntity<AssignSubmissionRequest> submitAssignment(UUID studentId, UUID openingId,
        MultipartFile multipartFile, String fileName) {
        try {
            AssignSubmissionRequest request = new AssignSubmissionRequest(studentId, openingId,
                multipartFile.getBytes(), fileName);

            return ResponseEntity.ok(assignmentService.submitAssignment(request));
        } catch (RuntimeException | IOException e) {
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
}
