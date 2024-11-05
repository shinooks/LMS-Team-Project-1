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

    /**
     * 새로운 강의 개설을 생성하는 엔드포인트
     * @param courseOpeningDto 강의 개설 정보를 담은 DTO
     * @return 생성된 강의 개설 정보
     */
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

    /**
     * 모든 강의 개설 목록을 조회하는 엔드포인트
     * @return 전체 강의 개설 목록
     */
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

    /**
     * 특정 ID의 강의 개설을 조회하는 엔드포인트
     * @param openingId 조회할 강의 개설 ID
     * @return 해당 ID의 강의 개설 정보
     */
    @GetMapping("/{openingId}")
    public ResponseEntity<CourseOpeningDto> getCourseOpening(@PathVariable UUID openingId) {
        CourseOpeningDto opening = courseOpeningService.getCourseOpening(openingId);
        return ResponseEntity.ok(opening);
    }
}