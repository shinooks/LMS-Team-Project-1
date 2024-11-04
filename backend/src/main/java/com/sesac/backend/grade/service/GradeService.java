package com.sesac.backend.grade.service;

import com.sesac.backend.grade.domain.Grade;
import com.sesac.backend.grade.dto.requset.GradeUpdateRequestDto;
import com.sesac.backend.grade.dto.response.GradeResponseDto;
import com.sesac.backend.grade.repository.GradeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeRepository gradeRepository;

    /**
     * 성적 조회
     */
    public GradeResponseDto getGrade(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("성적을 찾을 수 없습니다."));
        return new GradeResponseDto(grade);
    }

    /**
     * 성적 수정
     */
    @Transactional
    public GradeResponseDto updateGrade(Long gradeId, GradeUpdateRequestDto requestDto) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("성적을 찾을 수 없습니다."));

        grade.updateScores(requestDto);
        return new GradeResponseDto(grade);
    }

    /**
     * 과제별 성적 목록 조회
     */
    public List<GradeResponseDto> getGradesByAssignment(Long assignmentId) {
        return gradeRepository.findByAssignmentDomeId(assignmentId)
                .stream()
                .map(GradeResponseDto::new)
                .collect(Collectors.toList());
    }
}