package com.sesac.backend.evaluation.assignment.controller;

import com.sesac.backend.evaluation.assignment.dto.AssignmentDto;
import com.sesac.backend.evaluation.assignment.service.AssignmentService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
     * Assignment 테이블 전체 조회
     *
     * @return List<AssignmentDto>
     */
    @GetMapping("")
    public ResponseEntity<List<AssignmentDto>> getAllAssignments() {
        try {
            return ResponseEntity.ok(assignmentService.findAll());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Assignment 테이블 레코드 assignId(PK)로 조회
     *
     * @param assignId
     * @return Map<String, AssignmentDto>
     */
    @GetMapping("/{assignId}")
    public ResponseEntity<AssignmentDto> getAssignmentById(
        @PathVariable("assignId") UUID assignId) {
        try {
            return ResponseEntity.ok(assignmentService.findById(assignId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Assignment 테이블 레코드 생성
     *
     * @param assignmentDto
     * @return ResponseEntity<AssignmentDto>
     */
    @PostMapping("")
    public ResponseEntity<AssignmentDto> createAssign(AssignmentDto assignmentDto) {
        try {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{assignId}")
                .buildAndExpand(assignmentDto.getAssignId())
                .toUri();

            return ResponseEntity.created(location)
                .body(assignmentService.createAssign(assignmentDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Assignment 테이블 레코드 수정
     *
     * @param assignmentDto
     * @return Map<String, Boolean>
     */
    @PutMapping("")
    public ResponseEntity<AssignmentDto> updateAssignment(AssignmentDto assignmentDto) {
        try {
            return ResponseEntity.ok(assignmentService.updateAssign(assignmentDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Assignment 테이블 레코드 assignId(PK)로 삭제
     *
     * @param assignId
     * @return Map<String, Boolean>
     */
    @DeleteMapping("/{assignId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable("assignId") UUID assignId) {
        try {
            assignmentService.delete(assignId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
