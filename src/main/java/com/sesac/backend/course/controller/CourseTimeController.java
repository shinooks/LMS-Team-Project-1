package com.sesac.backend.course.controller;

import com.sesac.backend.course.constant.DayOfWeek;
import com.sesac.backend.course.dto.CompleteUpdateRequestDto;
import com.sesac.backend.course.dto.CourseTimeDto;
import com.sesac.backend.course.service.CourseTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "CourseTime", description = "강의 시간 관리 API")
public class CourseTimeController {

    private final CourseTimeService courseTimeService;

    // 강의 시간 등록
    @Operation(
        summary = "강의 시간 등록",
        description = "특정 강의 개설에 대한 새로운 강의 시간을 등록합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 시간이 성공적으로 등록되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
    @Operation(
        summary = "전체 강의 시간 조회",
        description = "등록된 모든 강의 시간을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 시간을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
    @Operation(
        summary = "특정 강의 시간 조회",
        description = "특정 ID를 가진 강의 시간을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 시간을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "404", description = "강의 시간을 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
    @Operation(
        summary = "특정 강의 개설의 시간 조회",
        description = "특정 강의 개설에 대한 모든 강의 시간을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 시간을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "404", description = "강의 개설을 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
    @Operation(
        summary = "특정 요일의 강의 시간 조회",
        description = "특정 요일에 해당하는 모든 강의 시간을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 시간을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
    @Operation(
        summary = "특정 강의실의 시간표 조회",
        description = "특정 강의실과 선택된 요일에 대한 모든 강의 시간을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 시간을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
    @Operation(
        summary = "강의 시간 수정",
        description = "특정 ID를 가진 강의 시간을 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 시간이 성공적으로 수정되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "강의 시간을 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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
    @Operation(
        summary = "강의 시간 삭제",
        description = "특정 ID를 가진 강의 시간을 삭제합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의 시간이 성공적으로 삭제되었습니다.")
    @ApiResponse(responseCode = "404", description = "강의 시간을 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
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

    @PutMapping("/course-openings/{openingId}/complete-update")
    @Operation(summary = "강의 개설 및 시간 정보 동시 수정")
    public ResponseEntity<?> updateCourseOpeningAndTimes(
            @PathVariable UUID openingId,
            @RequestBody CompleteUpdateRequestDto requestDto
    ) {
        try {
            courseTimeService.updateCourseOpeningAndTimes(openingId, requestDto);
            return ResponseEntity.ok()
                    .body(Map.of("message", "강의 개설 정보가 성공적으로 수정되었습니다."));
        } catch (Exception e) {
            return handleException(e, "updating course opening and times");
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