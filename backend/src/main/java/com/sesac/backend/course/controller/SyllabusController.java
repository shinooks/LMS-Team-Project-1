package com.sesac.backend.course.controller;

import com.sesac.backend.course.dto.SyllabusDto;
import com.sesac.backend.course.service.SyllabusService;
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
@RequestMapping("/syllabi")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Syllabus", description = "강의 계획서 관리 API")
public class SyllabusController {

    private final SyllabusService syllabusService;

    // 강의계획서 생성
    @Operation(
        summary = "강의계획서 생성",
        description = "특정 강의 개설에 대한 새로운 강의계획서를 생성합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의계획서가 성공적으로 생성되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @PostMapping("/course-openings/{openingId}")
    public ResponseEntity<?> createSyllabus(
            @PathVariable UUID openingId,
            @RequestBody @Valid SyllabusDto syllabusDto) {
        try {
            log.info("Creating syllabus for opening id: {}", openingId);
            SyllabusDto createdSyllabus = syllabusService.createSyllabus(openingId, syllabusDto);
            return ResponseEntity.ok(createdSyllabus);
        } catch (Exception e) {
            return handleException(e, "creating syllabus");
        }
    }

    // 전체 강의계획서 조회
    @Operation(
        summary = "전체 강의계획서 조회",
        description = "등록된 모든 강의계획서를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의계획서를 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping
    public ResponseEntity<?> getAllSyllabi() {
        try {
            log.info("Fetching all syllabi");
            List<SyllabusDto> syllabi = syllabusService.getAllSyllabi();
            return ResponseEntity.ok(syllabi);
        } catch (Exception e) {
            return handleException(e, "fetching all syllabi");
        }
    }

    // 특정 강의 개설의 강의계획서 조회
    @Operation(
        summary = "특정 강의 개설의 강의계획서 조회",
        description = "특정 강의 개설에 대한 모든 강의계획서를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의계획서를 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "404", description = "강의 개설을 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/course-openings/{openingId}")
    public ResponseEntity<?> getSyllabusByOpeningId(@PathVariable UUID openingId) {
        try {
            log.info("Fetching syllabus for opening id: {}", openingId);
            SyllabusDto syllabus = syllabusService.getSyllabusByOpeningId(openingId);
            return ResponseEntity.ok(syllabus);
        } catch (Exception e) {
            return handleException(e, "fetching syllabus by opening id");
        }
    }

    // 특정 강의계획서 조회
    @Operation(
        summary = "특정 강의계획서 조회",
        description = "특정 ID를 가진 강의계획서를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의계획서를 ���공적으로 조회했습니다.")
    @ApiResponse(responseCode = "404", description = "강의계획서를 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/{syllabusId}")
    public ResponseEntity<?> getSyllabus(@PathVariable UUID syllabusId) {
        try {
            log.info("Fetching syllabus: {}", syllabusId);
            SyllabusDto syllabus = syllabusService.getSyllabus(syllabusId);
            return ResponseEntity.ok(syllabus);
        } catch (Exception e) {
            return handleException(e, "fetching syllabus");
        }
    }

    // 강의 계획서 수정
    @Operation(
        summary = "강의계획서 수정",
        description = "특정 ID를 가진 강의계획서를 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의계획서가 성공적으로 수정되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "강의계획서를 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @PutMapping("/{syllabusId}")
    public ResponseEntity<?> updateSyllabus(
            @PathVariable UUID syllabusId,
            @RequestBody @Valid SyllabusDto syllabusDto) {
        try {
            log.info("Updating syllabus: {}", syllabusDto);
            SyllabusDto updatedSyllabus = syllabusService.updateSyllabus(syllabusId, syllabusDto);
            return ResponseEntity.ok(updatedSyllabus);
        } catch (Exception e) {
            return handleException(e, "updating syllabus");
        }
    }

    // 강의 계획서 삭제
    @Operation(
        summary = "강의계획서 삭제",
        description = "특정 ID를 가진 강의계획서를 삭제합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의계획서가 성공적으로 삭제되었습니다.")
    @ApiResponse(responseCode = "404", description = "강의계획서를 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @DeleteMapping("/{syllabusId}")
    public ResponseEntity<?> deleteSyllabus(@PathVariable UUID syllabusId) {
        try {
            log.info("Deleting syllabus: {}", syllabusId);
            syllabusService.deleteSyllabus(syllabusId);
            return ResponseEntity.ok()
                    .body(Map.of("message", "강의계획서가 삭제되었습니다."));
        } catch (Exception e) {
            return handleException(e, "deleting syllabus");
        }
    }

    // 강의 계획서 존재 여부 확인
    @Operation(
        summary = "강의계획서 존재 여부 확인",
        description = "특정 강의 개설에 대한 강의계획서가 존재하는지 확인합니다."
    )
    @ApiResponse(responseCode = "200", description = "강의계획서 존재 여부를 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/exists/course-openings/{openingId}")
    public ResponseEntity<?> checkSyllabusExists(@PathVariable UUID openingId) {
        try {
            log.info("Checking syllabus existence for opening id: {}", openingId);
            boolean exists = syllabusService.existsSyllabusByOpeningId(openingId);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            return handleException(e, "checking syllabus existence");
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