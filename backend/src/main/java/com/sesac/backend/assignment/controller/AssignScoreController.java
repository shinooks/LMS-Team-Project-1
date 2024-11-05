package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.dto.AssignScoreDto;
import com.sesac.backend.assignment.service.AssignScoreService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public Map<String, AssignScoreDto> getScore(@PathVariable("id") UUID assignScoreId) {
        try {
            return Map.of("success", assignScoreService.findById(assignScoreId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", null);
        }
    }

    @GetMapping("")
    public List<AssignScoreDto> getAllScores() {
        return assignScoreService.findAll();
    }

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

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteScore(@PathVariable("id") UUID assignScoreId) {
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
