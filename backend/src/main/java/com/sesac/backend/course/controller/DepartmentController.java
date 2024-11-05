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

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(departmentService.createDepartment(departmentDto));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    /**
     * 학과 정보를 수정하는 엔드포인트
     */
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

    /**
     * 학과를 삭제하는 엔드포인트
     */
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