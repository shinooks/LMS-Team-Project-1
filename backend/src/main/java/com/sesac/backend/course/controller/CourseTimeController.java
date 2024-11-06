package com.sesac.backend.course.controller;

import com.sesac.backend.course.dto.CourseTimeDto;
import com.sesac.backend.course.service.CourseTimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/course-times")
@RequiredArgsConstructor
@Slf4j
public class CourseTimeController {

    private final CourseTimeService courseTimeService;

    // 강의 시간 등록
    @PostMapping
    public ResponseEntity<?> createCourseTime(@RequestBody @Valid CourseTimeDto courseTimeDto) {
        try {
            log.info("Creating course time: {}", courseTimeDto);
            CourseTimeDto createdTime = courseTimeService.createCourseTime(courseTimeDto);
            return ResponseEntity.ok(createdTime);
        } catch (Exception e) {
            log.error("Error creating course time", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 모든 강의 시간 조회
    @GetMapping
    public ResponseEntity<?> getAllCourseTimes() {
        try {
            List<CourseTimeDto> times = courseTimeService.getAllCourseTimes();
            return ResponseEntity.ok(times);
        } catch (Exception e) {
            log.error("Error fetching course times", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 특정 강의 시간 조회
    @GetMapping("/{timeId}")
    public ResponseEntity<CourseTimeDto> getCourseTime(@PathVariable UUID timeId) {
        CourseTimeDto time = courseTimeService.getCourseTime(timeId);
        return ResponseEntity.ok(time);
    }

    // 강의 시간 수정
    @PutMapping("/{timeId}")
    public ResponseEntity<?> updateCourseTime(
            @PathVariable UUID timeId,
            @RequestBody @Valid CourseTimeDto courseTimeDto) {
        try {
            log.info("Updating course time: {}", courseTimeDto);
            CourseTimeDto updatedTime = courseTimeService.updateCourseTime(timeId, courseTimeDto);
            return ResponseEntity.ok(updatedTime);
        } catch (Exception e) {
            log.error("Error updating course time", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 강의 시간 삭제
    @DeleteMapping("/{timeId}")
    public ResponseEntity<?> deleteCourseTime(@PathVariable UUID timeId) {
        try {
            log.info("Deleting course time with id: {}", timeId);
            courseTimeService.deleteCourseTime(timeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting course time", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}