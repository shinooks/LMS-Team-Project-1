package com.sesac.backend.grade.service;


import com.sesac.backend.course.controller.CourseService;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.Grade;
import com.sesac.backend.grade.dto.GradeDto;
import com.sesac.backend.grade.repository.GradeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeRepository gradeRepository;
    private final CourseService courseService;


    // 학생 ID로 조회
    public GradeDto findById(UUID gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 성적 정보가 없습니다. id=" + gradeId));
        return GradeDto.from(grade);
    }


    // 전체 조회 - 강의, 학기를 선택 한 후 학번 , 이름 점수 등이 조회되게
    public List<GradeDto> findAllByCourse(String courseName, String term) {
        // 1차 필터링: 선택한 강의명, 학기에 해당하는 과목 조회 -> 선택한 강의명, 학기에 해당되는 강의 리스트가 나옴
        List<Course> courses = courseService.findAllByCourseNameAndTerm(courseName, term);
        // 2차 필터링: 각 과목에 해당하는 성적 조회 -> 각 과목에 해당되는 성적 리스트가 나옴

        List<Grade> grades = new ArrayList<>();
        for (Course course : courses) {
            grades.addAll(gradeRepository.findAllByCourse(course));
        }

        return grades.stream()
                .map(GradeDto::from)
                .collect(Collectors.toList());
    }
    }
    // 입력 , 수정




    // 삭제



}
