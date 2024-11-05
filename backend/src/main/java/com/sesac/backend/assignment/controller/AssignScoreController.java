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
@RequestMapping("/assignments/score")
@RestController
public class AssignScoreController {

    private final AssignScoreService assignScoreService;

    @Autowired
    public AssignScoreController(AssignScoreService assignScoreService) {
        this.assignScoreService = assignScoreService;
    }

    @GetMapping("/{id}")
    public Map<String, AssignScoreDto> getScore(@PathVariable("id") UUID assignScoreId) {
        return Map.of("success", assignScoreService.findById(assignScoreId));
    }

    @GetMapping("")
    public List<AssignScoreDto> getAllScores() {
        return assignScoreService.findAll();
    }

    @PostMapping("")
    public AssignScoreDto addScore(AssignScoreDto assignScoreDto) {
        return assignScoreService.save(assignScoreDto);
    }

    @PutMapping("")
    public AssignScoreDto updateScore(AssignScoreDto assignScoreDto) {
        AssignScoreDto saved = assignScoreService.findById(assignScoreDto.getAssignScoreId());
        saved.setScore(assignScoreDto.getScore());
        saved.setVisibility(assignScoreDto.getVisibility());
        return assignScoreService.save(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteScore(@PathVariable("id") UUID assignScoreId) {
        assignScoreService.delete(assignScoreId);
    }
}
