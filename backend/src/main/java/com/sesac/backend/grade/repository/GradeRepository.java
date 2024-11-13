package com.sesac.backend.grade.repository;

import com.sesac.backend.assignment.domain.Score;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * 성적 정보 레포지토리
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, UUID> {
//    List<Grade> findAllByCourseCourseNameAndCourseOpeningSemester(String courseName, String semester);

    List<Grade> findAllByCourseOpeningSemesterAndCourseOpeningCourseCourseName(String semester, String courseName);

    // Score ID로 Grade 목록 조회
    List<Grade> findByScore_ScoreId(UUID scoreId);

    // CourseOpening ID로 Grade 목록 조회
    List<Grade> findByCourseOpening_OpeningId(UUID openingId);






}
