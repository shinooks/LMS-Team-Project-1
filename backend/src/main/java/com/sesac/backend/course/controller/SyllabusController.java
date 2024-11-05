package com.sesac.backend.course.controller;

import com.sesac.backend.course.dto.SyllabusDto;
import com.sesac.backend.course.service.SyllabusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/syllabi")
@RequiredArgsConstructor
@Slf4j
public class SyllabusController {

    private final SyllabusService syllabusService;

    /**
     * 새로운 강의 계획서를 생성하는 엔드포인트
     */
    @PostMapping("/course-openings/{openingId}")
    public ResponseEntity<?> createSyllabus(
            @PathVariable UUID openingId,
            @RequestBody @Valid SyllabusDto syllabusDto) {
        try {
            SyllabusDto createdSyllabus = syllabusService.createSyllabus(openingId, syllabusDto);
            return ResponseEntity.ok(createdSyllabus);
        } catch (Exception e) {
            log.error("Error creating syllabus", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * 강의 개설 ID로 강의 계획서를 조회하는 엔드포인트 추가
     */
    @GetMapping("/course-openings/{openingId}")
    public ResponseEntity<?> getSyllabusByOpeningId(@PathVariable UUID openingId) {
        try {
            SyllabusDto syllabus = syllabusService.getSyllabusByOpeningId(openingId);
            return ResponseEntity.ok(syllabus);
        } catch (Exception e) {
            log.error("Error fetching syllabus by opening id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * 특정 강의 계획서를 조회하는 엔드포인트
     */
    @GetMapping("/{syllabusId}")
    public ResponseEntity<SyllabusDto> getSyllabus(@PathVariable UUID syllabusId) {
        SyllabusDto syllabus = syllabusService.getSyllabus(syllabusId);
        return ResponseEntity.ok(syllabus);
    }
}