package com.sesac.backend.course.controller;

import com.sesac.backend.course.constant.CourseStatus;
import com.sesac.backend.course.dto.CourseOpeningDto;
import com.sesac.backend.course.service.CourseOpeningService;
import com.sesac.backend.entity.Professor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "CourseOpening", description = "강의 개설 관리 API")
public class CourseOpeningController {

    private final CourseOpeningService courseOpeningService;

    // 강의 개설 생성
    @Operation(
        summary = "강의 개설 생성",
        description = "새로운 강의 개설을 생성합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 개설이 성공적으로 생성되었습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 전체 강의 개설 목록 조회
    @Operation(
        summary = "전체 강의 개설 목록 조회",
        description = "등록된 모든 강의 개설을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 개설 목록을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
    @Operation(
        summary = "특정 강의 개설 조회",
        description = "특정 ID를 가진 강의 개설을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 개설을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/{openingId}")
    public ResponseEntity<?> getCourseOpening(@PathVariable UUID openingId) {
        try {
            log.info("Fetching course opening with id: {}", openingId);
            CourseOpeningDto opening = courseOpeningService.getCourseOpening(openingId);
            return ResponseEntity.ok(opening);
        } catch (Exception e) {
            log.error("Error fetching course opening", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 특정 강의의 개설 목록 조회
    @Operation(
        summary = "특정 강의의 개설 목록 조회",
        description = "특정 강의에 대한 모든 개설 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 개설 목록을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getCourseOpeningsByCourse(@PathVariable UUID courseId) {
        try {
            log.info("Fetching course openings for course: {}", courseId);
            List<CourseOpeningDto> openings = courseOpeningService.getCourseOpeningsByCourse(courseId);
            return ResponseEntity.ok(openings);
        } catch (Exception e) {
            log.error("Error fetching course openings by course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }


    // 특정 상태의 강의 목록 조회
    @Operation(
        summary = "특정 상태의 강의 목록 조회",
        description = "특정 상태에 해당하는 모든 강의 개설을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "상태별 강의 목록을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getCoursesByStatus(@PathVariable CourseStatus status) {
        try {
            log.info("Fetching courses by status: {}", status);
            List<CourseOpeningDto> courses = courseOpeningService.getCoursesByStatus(status);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("Error fetching courses by status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 강의 개설 수정
    @Operation(
        summary = "강의 개설 수정",
        description = "특정 ID를 가진 강의 개설을 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 개설이 성공적으로 수정되었습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }

    // 강의 개설 삭제
    @Operation(
        summary = "강의 개설 삭제",
        description = "특정 ID를 가진 강의 개설을 삭제합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 개설이 성공적으로 삭제되었습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @DeleteMapping("/{openingId}")
    public ResponseEntity<?> deleteCourseOpening(@PathVariable UUID openingId) {
        try {
            log.info("Deleting course opening with id: {}", openingId);
            courseOpeningService.deleteCourseOpening(openingId);
            return ResponseEntity.ok(Map.of("message", "강의 개설이 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("Error deleting course opening", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "error", e.getClass().getSimpleName()
                    ));
        }
    }
}