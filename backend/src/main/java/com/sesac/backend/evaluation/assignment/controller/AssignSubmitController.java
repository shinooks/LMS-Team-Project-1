package com.sesac.backend.evaluation.assignment.controller;

import com.sesac.backend.evaluation.assignment.dto.AssignSubmitDto;
import com.sesac.backend.evaluation.assignment.service.AssignSubmitService;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author dongjin
 * 과제 제출 Controller
 * http 요청을 받아 AssignSubmit service 호출
 */
@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignments/submit")
@RestController
public class AssignSubmitController {

    private final AssignSubmitService assignSubmitService;

    @Autowired
    public AssignSubmitController(AssignSubmitService assignSubmitService) {
        this.assignSubmitService = assignSubmitService;
    }

    /**
     * AssignSubmit 테이블 전체 조회
     * @return List<AssignSubmitDto>
     */
    @GetMapping("")
    public ResponseEntity<List<AssignSubmitDto>> getAllSubmits() {
        return ResponseEntity.ok(assignSubmitService.getAll());
    }

    /**
     * AssignSubmit 테이블 레코드 assignSubmitId로 조회
     * @param assignSubmitId
     * @return Map<String, AssignSubmitDto>
     */
    @GetMapping("/{assignSubmitId}")
    public ResponseEntity<AssignSubmitDto> getAssignSubmit(@PathVariable("assignSubmitId") UUID assignSubmitId) {
        try {
            return ResponseEntity.ok(assignSubmitService.findById(assignSubmitId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * AssignSubmit 테이블 레코드 추가 (제출 기능)
     * @param assignSubmitDto
     * @return Map<String, Boolean>
     */
    @PostMapping("")
    public ResponseEntity<AssignSubmitDto> submit(AssignSubmitDto assignSubmitDto) {
        try {
            return ResponseEntity.ok(assignSubmitService.submit(assignSubmitDto));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * AssignSubmit 테이블 레코드 assignSubmitId로 삭제
     * @param assignSubmitId
     * @return Map<String, Boolean>
     */
    @DeleteMapping("/{assignSubmitId}")
    public ResponseEntity<Void> delete(@PathVariable("assignSubmitId") UUID assignSubmitId) {
        try {
            assignSubmitService.delete(assignSubmitId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
