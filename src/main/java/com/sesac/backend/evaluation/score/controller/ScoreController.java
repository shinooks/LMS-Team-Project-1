package com.sesac.backend.evaluation.score.controller;

import com.sesac.backend.evaluation.score.dto.ScoreDetailResponse;
import com.sesac.backend.evaluation.score.dto.ScoreDto;
import com.sesac.backend.evaluation.score.service.ScoreService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/scores")
@RestController
public class ScoreController {

    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ScoreDetailResponse> getScore(@PathVariable UUID examId) {
        try {
            return ResponseEntity.ok(scoreService.findResult(examId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ScoreDto>> getAll() {
        return ResponseEntity.ok(scoreService.findAll());
    }

    @PostMapping("")
    public ResponseEntity<ScoreDto> createScore(ScoreDto scoreDto) {
        try {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{scoreId}")
                .buildAndExpand(scoreDto.getScoreId())
                .toUri();

            return ResponseEntity.created(location)
                .body(scoreService.createScore(scoreDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("")
    public ResponseEntity<ScoreDto> updateScore(ScoreDto scoreDto) {
        try {
            return ResponseEntity.ok(scoreService.updateScore(scoreDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteScore(@PathVariable UUID examId) {
        try {
            scoreService.deleteScore(examId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
