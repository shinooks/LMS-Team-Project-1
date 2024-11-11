package com.sesac.backend.evaluation.exam.controller;

import com.sesac.backend.evaluation.exam.dto.ExamDto;
import com.sesac.backend.evaluation.exam.dto.ExamProblemDto;
import com.sesac.backend.evaluation.exam.service.ExamService;
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
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamDto> getFinalExam(@PathVariable UUID examId) {
        try {
            return ResponseEntity.ok(examService.getByExamId(examId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ExamDto>> getAllFinalExams() {
        try {
            return ResponseEntity.ok(examService.getAllExams());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<ExamDto> createFinalExam(ExamDto examDto) {
        try {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{examId}")
                .buildAndExpand(examDto.getExamId())
                .toUri();

            return ResponseEntity.created(location)
                .body(examService.createExam(examDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("")
    public ResponseEntity<ExamDto> updateFinalExam(ExamDto examDto) {
        try {
            return ResponseEntity.ok(examService.updateExam(examDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteFinalExam(@PathVariable UUID examId) {
        try {
            examService.deleteExam(examId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/problems/{finalExamId}")
    public ResponseEntity<List<ExamProblemDto>> getAllFinalExamProblems(@PathVariable("finalExamId") UUID finalExamId) {
        try {
            return ResponseEntity.ok(examService.findAllExamProblems(finalExamId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/problems")
    public ResponseEntity<List<ExamProblemDto>> createFinalExamProblems(UUID finalExamId,
        List<ExamProblemDto> examProblemDtos) {
        try {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{finalExamId}")
                .buildAndExpand(finalExamId)
                .toUri();

            return ResponseEntity.created(location)
                .body(examService.createExamProblems(finalExamId, examProblemDtos));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
