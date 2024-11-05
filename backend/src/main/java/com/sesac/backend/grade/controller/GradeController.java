package com.sesac.backend.grade.controller;

import java.util.List;

import java.util.UUID;  // 이 import 추가
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.sesac.backend.grade.dto.GradeDto;
import com.sesac.backend.grade.service.GradeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    // 단일 성적 조회
    @GetMapping("/{gradeId}")
    public ResponseEntity<GradeDto> getGrade(@PathVariable Long gradeId) {
        return ResponseEntity.ok(gradeService.findById(gradeId));
    }

    // 강의, 학기별 전체 성적 조회
    @GetMapping("/department/{departmentId}/term/{term}")

    }
}