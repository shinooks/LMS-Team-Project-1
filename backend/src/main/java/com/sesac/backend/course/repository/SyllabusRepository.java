package com.sesac.backend.course.repository;

import com.sesac.backend.course.constant.CourseStatus;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, UUID> {
    // 특정 강의 개설의 강의계획서 조회
    Optional<Syllabus> findByCourseOpeningOpeningId(UUID openingId);

    // 특정 연도/학기의 모든 강의계획서 조회
    List<Syllabus> findByCourseOpeningYearAndCourseOpeningSemester(Integer year, String semester);

    // 특정 교수의 모든 강의계획서 조회
    List<Syllabus> findByCourseOpeningProfessorId(String professorId);

    // 특정 강의 개설의 강의계획서 존재 여부 확인
    boolean existsByCourseOpeningOpeningId(UUID openingId);
}