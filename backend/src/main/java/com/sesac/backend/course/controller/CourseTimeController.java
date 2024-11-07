package com.sesac.backend.course.controller;

import com.sesac.backend.course.constant.DayOfWeek;
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
    @PostMapping("/course-openings/{openingId}")
    public ResponseEntity<?> createCourseTime(
            @PathVariable UUID openingId,
            @RequestBody @Valid CourseTimeDto courseTimeDto) {
        try {
            log.info("Creating course time for opening id: {}", openingId);
            CourseTimeDto createdTime = courseTimeService.createCourseTime(openingId, courseTimeDto);
            return ResponseEntity.ok(createdTime);
        } catch (Exception e) {
            return handleException(e, "creating course time");
        }
    }


    // 전체 강의 시간 조회
    @GetMapping
    public ResponseEntity<?> getAllCourseTimes() {
        try {
            log.info("Fetching all course times");
            List<CourseTimeDto> times = courseTimeService.getAllCourseTimes();
            return ResponseEntity.ok(times);
        } catch (Exception e) {
            return handleException(e, "fetching all course times");
        }
    }

    // 특정 강의 시간 조회
    @GetMapping("/{timeId}")
    public ResponseEntity<?> getCourseTime(@PathVariable UUID timeId) {
        try {
            log.info("Fetching course time: {}", timeId);
            CourseTimeDto time = courseTimeService.getCourseTime(timeId);
            return ResponseEntity.ok(time);
        } catch (Exception e) {
            return handleException(e, "fetching course time");
        }
    }

    // 특정 강의 개설의 시간 조회
    @GetMapping("/course-openings/{openingId}")
    public ResponseEntity<?> getCourseTimesByOpeningId(@PathVariable UUID openingId) {
        try {
            log.info("Fetching course times for opening: {}", openingId);
            List<CourseTimeDto> times = courseTimeService.getCourseTimesByOpeningId(openingId);
            return ResponseEntity.ok(times);
        } catch (Exception e) {
            return handleException(e, "fetching course times for opening");
        }
    }

    // 특정 요일의 강의 시간 조회
    @GetMapping("/day/{dayOfWeek}")
    public ResponseEntity<?> getCourseTimesByDay(@PathVariable DayOfWeek dayOfWeek) {
        try {
            log.info("Fetching course times for day: {}", dayOfWeek);
            List<CourseTimeDto> times = courseTimeService.getCourseTimesByDay(dayOfWeek);
            return ResponseEntity.ok(times);
        } catch (Exception e) {
            return handleException(e, "fetching course times for day");
        }
    }

    // 특정 강의실의 시간표 조회
    @GetMapping("/classroom/{classroom}")
    public ResponseEntity<?> getCourseTimesByClassroom(
            @PathVariable String classroom,
            @RequestParam(required = false) DayOfWeek dayOfWeek) {
        try {
            log.info("Fetching course times for classroom: {}, day: {}", classroom, dayOfWeek);
            List<CourseTimeDto> times = courseTimeService.getCourseTimesByClassroom(classroom, dayOfWeek);
            return ResponseEntity.ok(times);
        } catch (Exception e) {
            return handleException(e, "fetching course times for classroom");
        }
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
            return handleException(e, "updating course time");
        }
    }

    // 강의 시간 삭제
    @DeleteMapping("/{timeId}")
    public ResponseEntity<?> deleteCourseTime(@PathVariable UUID timeId) {
        try {
            log.info("Deleting course time: {}", timeId);
            courseTimeService.deleteCourseTime(timeId);
            return ResponseEntity.ok()
                    .body(Map.of("message", "강의 시간이 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            return handleException(e, "deleting course time");
        }
    }

    // 공통 예외 처리
    private ResponseEntity<?> handleException(Exception e, String operation) {
        log.error("Error while " + operation + ": {}", e.getMessage());
        HttpStatus status = e instanceof RuntimeException
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status)
                .body(Map.of("message", e.getMessage()));
    }
}