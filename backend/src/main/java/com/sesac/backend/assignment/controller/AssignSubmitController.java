package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.dto.AssignSubmitDto;
import com.sesac.backend.assignment.service.AssignSubmitService;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dongjin
 * 과제 제출 Controller
 * http 요청을 받아 AssignSubmit service 호출
 */
@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignment/submit")
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
    public List<AssignSubmitDto> getAllSubmits() {
        return assignSubmitService.getAll();
    }

    /**
     * AssignSubmit 테이블 레코드 assignSubmitId로 조회
     * @param assignSubmitId
     * @return Map<String, AssignSubmitDto>
     */
    @GetMapping("/{assignSubmitId}")
    public Map<String, AssignSubmitDto> getById(@PathVariable("assignSubmitId") UUID assignSubmitId) {
        try {
            return Map.of("success", assignSubmitService.findById(assignSubmitId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", null);
        }
    }

    /**
     * AssignSubmit 테이블 레코드 추가 (제출 기능)
     * @param assignSubmitDto
     * @return Map<String, Boolean>
     */
    @PostMapping("")
    public Map<String, Boolean> submit(AssignSubmitDto assignSubmitDto) {
        boolean flag = false;

        try {
            assignSubmitService.submit(assignSubmitDto);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", flag);
        }
    }

    /**
     * AssignSubmit 테이블 레코드 assignSubmitId로 삭제
     * @param assignSubmitId
     * @return Map<String, Boolean>
     */
    @DeleteMapping("/{assignSubmitId}")
    public Map<String, Boolean> delete(@PathVariable("assignSubmitId") UUID assignSubmitId) {
        boolean flag = false;

        try {
            assignSubmitService.delete(assignSubmitId);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", flag);
        }
    }
}
