package com.sesac.backend.evaluation.assignment.controller;

import com.sesac.backend.evaluation.assignment.dto.AssignCreationRequest;
import com.sesac.backend.evaluation.assignment.dto.AssignResponse;
import com.sesac.backend.evaluation.assignment.dto.AssignScoreRequest;
import com.sesac.backend.evaluation.assignment.dto.AssignSubmissionRequest;
import com.sesac.backend.evaluation.assignment.service.AssignmentService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author dongjin 과제 controller http 요청을 받아 Assignment service 호출
 */
@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignments")
@RestController
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
    public ResponseEntity<AssignCreationRequest> createAssignment(@RequestBody AssignCreationRequest assignCreationRequest) {
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
     * @param request
     * @return
     */
    @PostMapping("/submit")
    public ResponseEntity<AssignSubmissionRequest> submitAssignment(@RequestBody AssignSubmissionRequest request) {
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
    public ResponseEntity<AssignScoreRequest> submitAssignmentScore(@RequestBody AssignScoreRequest request) {
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
    @GetMapping("/{opneingId}/{studentId}")
    public ResponseEntity<AssignResponse> getAssignment(@PathVariable UUID openingId, @PathVariable UUID studentId) {
        try {
            return ResponseEntity.ok(assignmentService.findAssign(openingId, studentId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
