package com.sesac.backend.course.controller;

import com.sesac.backend.course.dto.CourseDto;
import com.sesac.backend.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto) {
        try {
            log.info("Creating course: {}", courseDto);
            CourseDto createdCourse = courseService.createCourse(courseDto);
            return ResponseEntity.ok(createdCourse);
        } catch (Exception e) {
            log.error("Error creating course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName(),
                            "stackTrace", e.getStackTrace()
                    ));
        }
    }
    // 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            log.info("Fetching all courses");
            List<CourseDto> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("Error fetching courses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }
    // 일부조회
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable UUID courseId) {
        CourseDto course = courseService.getCourse(courseId);
        return ResponseEntity.ok(course);
    }

    // 강의 수정
    @PutMapping("/{courseId}")
    public ResponseEntity<?> updateCourse(
            @PathVariable UUID courseId,
            @RequestBody CourseDto courseDto) {
        try {
            log.info("Updating course: {}", courseDto);
            CourseDto updatedCourse = courseService.updateCourse(courseId, courseDto);
            return ResponseEntity.ok(updatedCourse);
        } catch (Exception e) {
            log.error("Error updating course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 강의 삭제
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable UUID courseId) {
        try {
            log.info("Deleting course with id: {}", courseId);
            courseService.deleteCourse(courseId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}