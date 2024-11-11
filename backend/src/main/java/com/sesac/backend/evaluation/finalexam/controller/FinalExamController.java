package com.sesac.backend.evaluation.finalexam.controller;

import com.sesac.backend.evaluation.finalexam.dto.FinalExamDto;
import com.sesac.backend.evaluation.finalexam.dto.FinalExamProblemDto;
import com.sesac.backend.evaluation.finalexam.service.FinalExamService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author dongjin 기말고사 Controller http 요청을 받아 FinalExam service 호출
 */
@Slf4j
@CrossOrigin("*")
@RequestMapping("/exams/final")
@RestController
public class FinalExamController {

    private final FinalExamService finalExamService;

    public FinalExamController(FinalExamService finalExamService) {
        this.finalExamService = finalExamService;
    }

    @GetMapping("/{examId}")
    public ResponseEntity<FinalExamDto> getFinalExam(@PathVariable UUID examId) {
        try {
            return ResponseEntity.ok(finalExamService.getByExamId(examId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<FinalExamDto>> getAllFinalExams() {
        try {
            return ResponseEntity.ok(finalExamService.getAllFinalExams());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<FinalExamDto> createFinalExam(FinalExamDto finalExamDto) {
        try {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{examId}")
                .buildAndExpand(finalExamDto.getFinalExamId())
                .toUri();

            return ResponseEntity.created(location)
                .body(finalExamService.createFinalExam(finalExamDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("")
    public ResponseEntity<FinalExamDto> updateFinalExam(FinalExamDto finalExamDto) {
        try {
            return ResponseEntity.ok(finalExamService.updateFinalExam(finalExamDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteFinalExam(@PathVariable UUID examId) {
        try {
            finalExamService.deleteFinalExam(examId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/problems/{finalExamId}")
    public ResponseEntity<List<FinalExamProblemDto>> getAllFinalExamProblems(@PathVariable("finalExamId") UUID finalExamId) {
        try {
            return ResponseEntity.ok(finalExamService.findAllFinalExamProblems(finalExamId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/problems")
    public ResponseEntity<List<FinalExamProblemDto>> createFinalExamProblems(UUID finalExamId,
        List<FinalExamProblemDto> finalExamProblemDtos) {
        try {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{finalExamId}")
                .buildAndExpand(finalExamId)
                .toUri();

            return ResponseEntity.created(location)
                .body(finalExamService.createFinalExamProblems(finalExamId, finalExamProblemDtos));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
