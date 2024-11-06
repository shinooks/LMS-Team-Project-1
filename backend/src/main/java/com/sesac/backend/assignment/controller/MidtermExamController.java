package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.dto.MidtermExamDto;
import com.sesac.backend.assignment.service.MidtermExamService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/exams/midterm")
@RestController
public class MidtermExamController {

    private final MidtermExamService midtermExamService;

    @Autowired
    public MidtermExamController(MidtermExamService midtermExamService) {
        this.midtermExamService = midtermExamService;
    }

    @GetMapping("/{examId}")
    public ResponseEntity<MidtermExamDto> getMidtermExam(@PathVariable UUID examId) {
        try {
            return ResponseEntity.ok(midtermExamService.getMidtermExam(examId));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<MidtermExamDto>> getAllMidtermExams() {
        try {
            return ResponseEntity.ok(midtermExamService.getAllMidtermExams());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<MidtermExamDto> createMidtermExam(MidtermExamDto midtermExamDto) {
        try {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{examId}")
                .buildAndExpand(midtermExamDto.getMidtermExamId())
                .toUri();

            return ResponseEntity.created(location)
                .body(midtermExamService.createMidtermExam(midtermExamDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("")
    public ResponseEntity<MidtermExamDto> updateMidtermExam(MidtermExamDto midtermExamDto) {
        try {
            return ResponseEntity.ok(midtermExamService.updateMidtermExam(midtermExamDto));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteMidtermExam(@PathVariable UUID examId) {
        try {
            midtermExamService.deleteMidtermExam(examId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
