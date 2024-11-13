package com.sesac.backend.course.controller;

import com.sesac.backend.course.dto.DepartmentDto;
import com.sesac.backend.course.service.DepartmentService;
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
@RequestMapping("/departments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Department", description = "학과 관리 API")
public class DepartmentController {

    private final DepartmentService departmentService;

    // 학과 정보 등록
    @Operation(
        summary = "학과 정보 등록",
        description = "새로운 학과 정보를 등록합니다."
    )
    @ApiResponse(responseCode = "200", description = "학과 정보가 성공적으로 등록되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody @Valid DepartmentDto departmentDto) {
        try {
            log.info("Creating department: {}", departmentDto);
            DepartmentDto createdDepartment = departmentService.createDepartment(departmentDto);
            return ResponseEntity.ok(createdDepartment);
        } catch (Exception e) {
            log.error("Error creating department", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 전체 학과 정보 조회
    @Operation(
        summary = "전체 학과 정보 조회",
        description = "등록된 모든 학과 정보를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "학과 목록을 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping
    public ResponseEntity<?> getAllDepartments() {
        try {
            log.info("Fetching all departments");
            List<DepartmentDto> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            log.error("Error fetching departments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 특정 학과 정보 조회
    @Operation(
        summary = "특정 학과 정보 조회",
        description = "특정 ID를 가진 학과 정보를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "학과 정보를 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "404", description = "학과를 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/{departmentId}")
    public ResponseEntity<?> getDepartmentById(@PathVariable UUID departmentId) {
        try {
            log.info("Fetching department with ID: {}", departmentId);
            DepartmentDto department = departmentService.getDepartmentById(departmentId);
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            log.error("Error fetching department by ID", e);
            HttpStatus status = e instanceof RuntimeException ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 학과명으로 학과 정보 조회
    @Operation(
        summary = "학과명으로 정보 조회",
        description = "학과명을 기반으로 학과 정보를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "학과 정보를 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "404", description = "학과를 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/name/{departmentName}")
    public ResponseEntity<?> getDepartmentByName(@PathVariable String departmentName) {
        try {
            log.info("Fetching department with name: {}", departmentName);
            DepartmentDto department = departmentService.getDepartmentByName(departmentName);
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            log.error("Error fetching department by name", e);
            HttpStatus status = e instanceof RuntimeException ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 학과명 중복 확인
    @Operation(
        summary = "학과명 중복 확인",
        description = "입력한 학과명이 이미 존재하는지 확인합니다."
    )
    @ApiResponse(responseCode = "200", description = "중복 여부를 성공적으로 조회했습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @GetMapping("/check/{departmentName}")
    public ResponseEntity<?> checkDepartmentName(@PathVariable String departmentName) {
        try {
            log.info("Checking if department name exists: {}", departmentName);
            boolean exists = departmentService.isDepartmentNameExists(departmentName);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            log.error("Error checking department name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 학과 정보 수정
    @Operation(
        summary = "학과 정보 수정",
        description = "특정 ID를 가진 학과 정보를 수정합니다."
    )
    @ApiResponse(responseCode = "200", description = "학과 정보가 성공적으로 수정되었습니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @ApiResponse(responseCode = "404", description = "학과를 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @PutMapping("/{departmentId}")
    public ResponseEntity<?> updateDepartment(
            @PathVariable UUID departmentId,
            @RequestBody @Valid DepartmentDto departmentDto) {
        try {
            log.info("Updating department with ID: {}", departmentId);
            DepartmentDto updatedDepartment = departmentService.updateDepartment(departmentId, departmentDto);
            return ResponseEntity.ok(updatedDepartment);
        } catch (Exception e) {
            log.error("Error updating department", e);
            HttpStatus status = e instanceof RuntimeException ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }

    // 학과 정보 삭제
    @Operation(
        summary = "학과 정보 삭제",
        description = "특정 ID를 가진 학과 정보를 삭제합니다."
    )
    @ApiResponse(responseCode = "200", description = "학과가 성공적으로 삭제되었습니다.")
    @ApiResponse(responseCode = "404", description = "학과를 찾을 수 없습니다.")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류")
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable UUID departmentId) {
        try {
            log.info("Deleting department with ID: {}", departmentId);
            departmentService.deleteDepartment(departmentId);
            return ResponseEntity.ok(Map.of("message", "학과가 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("Error deleting department", e);
            HttpStatus status = e instanceof RuntimeException ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status)
                    .body(Map.of("message", e.getMessage(), "error", e.getClass().getSimpleName()));
        }
    }
}