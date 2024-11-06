package com.sesac.backend.course.controller;

import com.sesac.backend.course.constant.Credit;
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

    // 강의 등록
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
                            "error", e.getClass().getSimpleName()
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
    public ResponseEntity<?> getCourse(@PathVariable UUID courseId) {
        try {
            log.info("Fetching course with id: {}", courseId);
            CourseDto course = courseService.getCourse(courseId);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            log.error("Error fetching course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 학과별 강의 목록 조회
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getCoursesByDepartment(@PathVariable UUID departmentId) {
        try {
            log.info("Fetching courses for department: {}", departmentId);
            List<CourseDto> courses = courseService.getCoursesByDepartment(departmentId);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("Error fetching courses by department", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 학과의 전체 강의 수 조회
    @GetMapping("/count/department/{departmentId}")
    public ResponseEntity<?> countCoursesByDepartment(@PathVariable UUID departmentId) {
        try {
            log.info("Counting courses for department: {}", departmentId);
            long count = courseService.countCoursesByDepartment(departmentId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            log.error("Error counting courses by department", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 학점별 강의 목록 조회
    @GetMapping("/credit/{credit}")
    public ResponseEntity<?> getCoursesByCredits(@PathVariable Credit credit) {
        try {
            log.info("Fetching courses for credit: {}", credit);
            List<CourseDto> courses = courseService.getCoursesByCredits(credit);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("Error fetching courses by credits", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 강의 코드 중복 확인
    @GetMapping("/check/{courseCode}")
    public ResponseEntity<?> checkCourseCode(@PathVariable String courseCode) {
        try {
            log.info("Checking course code: {}", courseCode);
            boolean exists = courseService.isCourseCodeExists(courseCode);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            log.error("Error checking course code", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
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
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 강의 삭제
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable UUID courseId) {
        try {
            log.info("Deleting course with id: {}", courseId);
            courseService.deleteCourse(courseId);
            return ResponseEntity.ok(Map.of("message", "강의가 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("Error deleting course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
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