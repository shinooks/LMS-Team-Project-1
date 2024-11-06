package com.sesac.backend.course.controller;

import com.sesac.backend.course.dto.CourseOpeningDto;
import com.sesac.backend.course.service.CourseOpeningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/course-openings")
@RequiredArgsConstructor
@Slf4j
public class CourseOpeningController {

    private final CourseOpeningService courseOpeningService;

    // 강의 개설 생성
    @PostMapping
    public ResponseEntity<?> createCourseOpening(@RequestBody CourseOpeningDto courseOpeningDto) {
        try {
            log.info("Creating course opening: {}", courseOpeningDto);
            CourseOpeningDto createdOpening = courseOpeningService.createCourseOpening(courseOpeningDto);
            return ResponseEntity.ok(createdOpening);
        } catch (Exception e) {
            log.error("Error creating course opening", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName(),
                            "stackTrace", e.getStackTrace()
                    ));
        }
    }

    // 전체 강의 개설 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllCourseOpenings() {
        try {
            log.info("Fetching all course openings");
            List<CourseOpeningDto> openings = courseOpeningService.getAllCourseOpenings();
            return ResponseEntity.ok(openings);
        } catch (Exception e) {
            log.error("Error fetching course openings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 특정 강의 개설 조회
    @GetMapping("/{openingId}")
    public ResponseEntity<CourseOpeningDto> getCourseOpening(@PathVariable UUID openingId) {
        CourseOpeningDto opening = courseOpeningService.getCourseOpening(openingId);
        return ResponseEntity.ok(opening);
    }

    // 강의 개설 수정
    @PutMapping("/{openingId}")
    public ResponseEntity<?> updateCourseOpening(
            @PathVariable UUID openingId,
            @RequestBody CourseOpeningDto courseOpeningDto) {
        try {
            log.info("Updating course opening: {}", courseOpeningDto);
            CourseOpeningDto updatedOpening = courseOpeningService.updateCourseOpening(openingId, courseOpeningDto);
            return ResponseEntity.ok(updatedOpening);
        } catch (Exception e) {
            log.error("Error updating course opening", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 강의 개설 삭제
    @DeleteMapping("/{openingId}")
    public ResponseEntity<?> deleteCourseOpening(@PathVariable UUID openingId) {
        try {
            log.info("Deleting course opening with id: {}", openingId);
            courseOpeningService.deleteCourseOpening(openingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting course opening", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}