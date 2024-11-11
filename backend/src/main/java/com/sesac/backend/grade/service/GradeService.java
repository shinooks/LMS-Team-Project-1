package com.sesac.backend.grade.service;


import com.sesac.backend.assignment.domain.Score;
import com.sesac.backend.assignment.dto.ScoreDto;
import com.sesac.backend.assignment.repository.ScoreRepository;
import com.sesac.backend.assignment.service.ScoreService;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Grade;
import com.sesac.backend.grade.dto.GradeDto;
import com.sesac.backend.grade.dto.GradeUpdateRequest;
import com.sesac.backend.grade.repository.GradeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeRepository gradeRepository;
    private final ScoreService scoreService;


    /**
     * Grade 생성 - 다른 서비스에서 호출하여 사용
     */
    @Transactional
    public GradeDto createGrade(Score score, CourseOpening courseOpening) {
        Grade grade = new Grade();
        grade.setScore(score);
        grade.setCourseOpening(courseOpening);

        Grade savedGrade = gradeRepository.save(grade);
        return GradeDto.from(savedGrade);
    }



    // 학생 ID로 조회
    public GradeDto findById(UUID gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 성적 정보가 없습니다. id=" + gradeId));
        return GradeDto.from(grade);
    }


    // 전체 조회
    public List<GradeDto> findAllByCourseCourseNameAndCourseOpeningSemester(String courseName, String semester) {
        // 1차 필터링: 선택한 강의명, 학기에 해당하는 과목 조회 -> 선택한 강의명, 학기에 해당되는 강의 리스트가 나옴
        List<Grade> grades = gradeRepository.findAllByCourseOpeningSemesterAndCourseOpeningCourseCourseName(semester, courseName);
        // 2차 필터링: 각 과목에 해당하는 성적 조회 -> 각 과목에 해당되는 성적 리스트가 나옴
        // Comparator 사용 해서 정렬
        Collections.sort(grades, Comparator.comparing(
                grade -> -(grade.getAssignScore() +
                        grade.getMidtermScore() +
                        grade.getFinalScore())
        ));

        // Grade 엔티티를 GradeDto로 변환
        return grades.stream()
                .map(GradeDto::from)
                .collect(Collectors.toList());
    }

    //수정
    @Transactional
    public GradeDto updateGradeScores(GradeUpdateRequest request) {
        //1. Grade 엔티티 조회
        Grade grade = gradeRepository.findById(request.getGradeId())
                .orElseThrow(() -> new EntityNotFoundException("해당 성적 정보가 없습니다. id=" + request.getGradeId()));

        //2. Score 정보 업데이트를 위한 DTO 생성
        ScoreDto scoreDto = new ScoreDto(
                grade.getScore().getScoreId(),
                grade.getScore().getAssignment().getAssignId(),
                grade.getScore().getMidtermExam().getMidtermExamId(),
                grade.getScore().getFinalExam().getFinalExamId(),
                grade.getScore().getStudent().getStudentId(),
                request.getAssignScore(),
                request.getMidtermScore(),
                request.getFinalScore(),
                grade.getScore().getVisibility()
        );

        //3. ScoreService를 통해 점수 업데이트
        scoreService.updateScore(scoreDto);

        //4. 업데이트된 Grade 정보 변환
        return GradeDto.from(grade);
    }

    //여러 성적 일괄 수정
    @Transactional
    public List<GradeDto> updateMultipleGradeScores(List<GradeUpdateRequest> requests){
        return requests.stream()
                .map(this::updateGradeScores)
                .collect(Collectors.toList());
    }





    // 삭제



}
