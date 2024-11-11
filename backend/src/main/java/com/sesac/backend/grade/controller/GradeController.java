package com.sesac.backend.grade.controller;

import java.util.List;

import java.util.UUID;  // 이 import 추가
import java.util.List;


import com.sesac.backend.grade.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.sesac.backend.grade.service.GradeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    // 단일 성적 조회
    @GetMapping("/{gradeId}")
    public ResponseEntity<GradeDto> getGrade(@PathVariable UUID gradeId) {
        return ResponseEntity.ok(gradeService.findById(gradeId));
    }

    // 강의, 학기별 전체 성적 조회
    @GetMapping("/course")
    public ResponseEntity<List<GradeDto>> getAllGradesByCourseAndSemester(
            @RequestParam String courseName,
            @RequestParam String semester) {
//        log.error("courseName: {}, semester: {}", courseName, semester);
        List<GradeDto> grades = gradeService.findAllByCourseCourseNameAndCourseOpeningSemester(
                courseName, semester);
//        log.error("grades: {}", grades);
        return ResponseEntity.ok(grades);
    }
    //    http://localhost:8081/grades/course?courseName=Chemistry&semester=Fall

    // 여러 학생 성적 일괄 수정 API 수정
    @PutMapping("/scores/batch")
    public ResponseEntity<List<GradeDto>> updateMultipleGradeScores(
            @RequestBody List<GradeUpdateRequest> updateRequests) {
        List<GradeDto> updatedGrades = gradeService.updateMultipleGradeScores(updateRequests);
        return ResponseEntity.ok(updatedGrades);
    }
    //  http://localhost:8081/grades/scores/batch


    // 학점 계산
    @GetMapping("/gpa/{scoreId}")
    public ResponseEntity<GpaCalculationDto> calculateGpa(@PathVariable UUID scoreId) {
        return ResponseEntity.ok(gradeService.calculateGpa(scoreId));
    }
    // http://localhost:8081/grades/gpa/5a6b7c8d-9e0f-1a2b-3c4d-5e6f7a8b9c0d



    @GetMapping("/list/{openingId}")
    public ResponseEntity<List<GradeListResponseDto>> getGradeList(
            @PathVariable UUID openingId,
            @RequestParam(defaultValue = "totalScore") String sortBy) {
        return ResponseEntity.ok(gradeService.getGradeList(openingId, sortBy));
    }
    // http://localhost:8081/grades/list/9e0f1a2b-3c4d-5e6f-7a8b-9c0d1f2a3b4c











}















