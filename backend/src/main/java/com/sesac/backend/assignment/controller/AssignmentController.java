package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.dto.AssignmentDto;
import com.sesac.backend.assignment.service.AssignmentService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author dongjin
 * 과제 controller
 * http 요청을 받아 Assignment service 호출
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
     * @return List<AssignmentDto>
     */
    @GetMapping("")
    public List<AssignmentDto> getAllAssignments() {
        return assignmentService.getAll();
    }

    /**
     * Assignment 테이블 레코드 assignId(PK)로 조회
     * @param assignId
     * @return Map<String, AssignmentDto>
     */
    @GetMapping("/{assignId}")
    public Map<String, AssignmentDto> getAssignmentById(@PathVariable("assignId") UUID assignId) {
        try {
            return Map.of("success", assignmentService.findById(assignId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", null);
        }
    }

    /**
     * Assignment 테이블 레코드 생성
     * @param assignmentDto
     * @return Map<String, Boolean>
     */
    @PostMapping("")
    public Map<String, Boolean> addAssignment(AssignmentDto assignmentDto) {
        boolean flag = false;

        try {
            assignmentService.save(assignmentDto);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", flag);
        }
    }

    /**
     * Assignment 테이블 레코드 수정
     * @param assignmentDto
     * @return Map<String, Boolean>
     */
    @PutMapping("")
    public Map<String, Boolean> updateAssignment(AssignmentDto assignmentDto) {
        boolean flag = false;

        try {
            AssignmentDto saved = assignmentService.findById(assignmentDto.getAssignId());
            saved.setCourse(assignmentDto.getCourse());
            saved.setTitle(assignmentDto.getTitle());
            saved.setDescription(assignmentDto.getDescription());
            saved.setDeadline(assignmentDto.getDeadline());
            assignmentService.save(saved);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            return Map.of("success", flag);
        }
    }

    /**
     * Assignment 테이블 레코드 assignId(PK)로 삭제
     * @param assignId
     * @return Map<String, Boolean>
     */
    @DeleteMapping("/{assignId}")
    public Map<String, Boolean> deleteAssignment(@PathVariable("assignId") UUID assignId) {
        boolean flag = false;

        try {
            assignmentService.delete(assignId);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            return Map.of("success", flag);
        }
    }
}
