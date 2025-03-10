package com.sesac.backend.course.controller;

import com.sesac.backend.course.dto.CourseOpeningDto;
import com.sesac.backend.course.dto.ProfessorDto;
import com.sesac.backend.course.service.ProfessorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/professors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Professor", description = "교수 관리 API")
public class ProfessorController {

    private final ProfessorService professorService;

    // 전체 교수 목록 조회
    @GetMapping
    public ResponseEntity<List<ProfessorDto>> getAllProfessors() {
        return ResponseEntity.ok(professorService.getAllProfessors());
    }

    // 특정 교수 정보 조회
    @GetMapping("/{professorId}")
    public ResponseEntity<ProfessorDto> getProfessor(@PathVariable UUID professorId) {
        return ResponseEntity.ok(professorService.getProfessor(professorId));
    }

    // 교수 번호로 교수 조회
    @GetMapping("/number/{professorNumber}")
    public ResponseEntity<ProfessorDto> getProfessorByNumber(@PathVariable String professorNumber) {
        return ResponseEntity.ok(professorService.getProfessorByNumber(professorNumber));
    }


    // 특정 교수의 전체 강의 목록 조회
    @GetMapping("/{professorId}/courses")
    public ResponseEntity<List<CourseOpeningDto>> getAllProfessorCourses(
            @PathVariable UUID professorId) {
        return ResponseEntity.ok(professorService.getAllProfessorCourses(professorId));
    }

    // 특정 교수의 특정 학기 강의 목록 조회
    @GetMapping("/{professorId}/courses/semester")
    public ResponseEntity<List<CourseOpeningDto>> getProfessorCoursesBySemester(
            @PathVariable UUID professorId,
            @RequestParam Integer year,
            @RequestParam String semester) {
        return ResponseEntity.ok(
                professorService.getProfessorCoursesBySemester(professorId, year, semester));
    }

    // 학과별 교수 목록 조회
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<ProfessorDto>> getProfessorsByDepartment(
            @PathVariable UUID departmentId) {
        return ResponseEntity.ok(professorService.getProfessorsByDepartment(departmentId));
    }

    // 이름으로 교수 검색
    @GetMapping("/search")
    public ResponseEntity<List<ProfessorDto>> getProfessorsByName(
            @RequestParam String name) {
        return ResponseEntity.ok(professorService.getProfessorsByName(name));
    }
}