package com.sesac.backend.grade.service;


import com.sesac.backend.entity.Grade;
import com.sesac.backend.grade.dto.GradeDto;
import com.sesac.backend.grade.repository.GradeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeRepository gradeRepository;


    // 학생 ID로 조회
    public GradeDto findById(UUID gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 성적 정보가 없습니다. id=" + gradeId));
        return GradeDto.from(grade);
    }


    // 전체 조회
    public List<GradeDto> findAllByCourseCourseNameAndCourseOpeningSemester(String courseName, String semester) {
        // 1차 필터링: 선택한 강의명, 학기에 해당하는 과목 조회 -> 선택한 강의명, 학기에 해당되는 강의 리스트가 나옴
        List<Grade> grades = gradeRepository.findAllByCourseCourseNameAndCourseOpeningSemester(courseName, semester);
        // 2차 필터링: 각 과목에 해당하는 성적 조회 -> 각 과목에 해당되는 성적 리스트가 나옴
        // Comparator 사용 해서 정렬
        Collections.sort(grades, Comparator.comparing(
                grade -> -(grade.getAssignScore().getAssignmentScore() +
                        grade.getAssignScore().getMidScore() +
                        grade.getAssignScore().getFinalScore())
        ));

        // 2차 필터링: Grade 엔티티를 GradeDto로 변환
        return grades.stream()
                .map(GradeDto::from)
                .collect(Collectors.toList());
    }



    //수정







    // 삭제



}
