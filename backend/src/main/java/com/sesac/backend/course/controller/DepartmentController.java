package com.sesac.backend.course.controller;

import com.sesac.backend.course.dto.DepartmentDto;
import com.sesac.backend.course.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    // 학과 정보 등록
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(departmentService.createDepartment(departmentDto));
    }

    // 전체 학과 정보 조회
    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // 특정 학과 정보 조회
    @GetMapping("/{departmentId}")
    public ResponseEntity<?> getDepartmentById(@PathVariable UUID departmentId) {
        try {
            DepartmentDto department = departmentService.getDepartmentById(departmentId);
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            log.error("Error fetching department", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 학과명으로 학과 정보 조회
    @GetMapping("/name/{departmentName}")
    public ResponseEntity<?> getDepartmentByName(@PathVariable String departmentName) {
        try {
            DepartmentDto department = departmentService.getDepartmentByName(departmentName);
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            log.error("Error fetching department by name", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 학과명 중복 확인
    @GetMapping("/check/{departmentName}")
    public ResponseEntity<?> checkDepartmentName(@PathVariable String departmentName) {
        boolean exists = departmentService.isDepartmentNameExists(departmentName);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // 학과 정보 수정
    @PutMapping("/{departmentId}")
    public ResponseEntity<?> updateDepartment(
            @PathVariable UUID departmentId,
            @RequestBody @Valid DepartmentDto departmentDto) {
        try {
            DepartmentDto updatedDepartment = departmentService.updateDepartment(departmentId, departmentDto);
            return ResponseEntity.ok(updatedDepartment);
        } catch (Exception e) {
            log.error("Error updating department", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 학과 정보 삭제
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<?> deleteDepartment(@PathVariable UUID departmentId) {
        try {
            departmentService.deleteDepartment(departmentId);
            return ResponseEntity.ok(Map.of("message", "학과가 성공적으로 삭제되었습니다."));
        } catch (Exception e) {
            log.error("Error deleting department", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}