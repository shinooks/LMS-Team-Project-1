package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.dto.AssignScoreDto;
import com.sesac.backend.assignment.service.AssignScoreService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dongjin
 * 과제 점수 controller
 * http 요청을 받아 AssignScore service 호출
 */
@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignments/scores")
@RestController
public class AssignScoreController {

    private final AssignScoreService assignScoreService;

    @Autowired
    public AssignScoreController(AssignScoreService assignScoreService) {
        this.assignScoreService = assignScoreService;
    }

    /**
     * AssignScore 테이블 레코드 assignScoreId(PK)로 조회
     * @param assignScoreId
     * @return Map<String, AssignScoreDto>
     */
    @GetMapping("/{assignScoreId}")
    public Map<String, AssignScoreDto> getScore(@PathVariable("assignScoreId") UUID assignScoreId) {
        try {
            return Map.of("success", assignScoreService.findById(assignScoreId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", null);
        }
    }

    /**
     * AssignScore 테이블 전체 조회
     * @return List<AssignScoreDto>
     */
    @GetMapping("")
    public List<AssignScoreDto> getAllScores() {
        return assignScoreService.getAll();
    }

    /**
     * AssignScore 테이블 레코드 생성
     * @param assignScoreDto
     * @return Map<String, Boolean>
     */
    @PostMapping("")
    public Map<String, Boolean> addScore(AssignScoreDto assignScoreDto) {
        boolean flag = false;

        try {
            assignScoreService.save(assignScoreDto);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", flag);
        }
    }

    /**
     * AssignScore 테이블 레코드 업데이트
     * @param assignScoreDto
     * @return Map<String, Boolean>
     */
    @PutMapping("")
    public Map<String, Boolean> updateScore(AssignScoreDto assignScoreDto) {
        boolean flag = false;

        try {
            AssignScoreDto saved = assignScoreService.findById(assignScoreDto.getAssignScoreId());
            saved.setScore(assignScoreDto.getScore());
            saved.setComment(assignScoreDto.getComment());
            saved.setVisibility(assignScoreDto.getVisibility());
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", flag);
        }
    }

    /**
     * AssignScore 테이블 레코드 assignScoreId로 삭제
     * @param assignScoreId
     * @return Map<String, Boolean>
     */
    @DeleteMapping("/{assignScoreId}")
    public Map<String, Boolean> deleteScore(@PathVariable("assignScoreId") UUID assignScoreId) {
        boolean flag = false;

        try {
            assignScoreService.delete(assignScoreId);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", flag);
        }
    }
}
