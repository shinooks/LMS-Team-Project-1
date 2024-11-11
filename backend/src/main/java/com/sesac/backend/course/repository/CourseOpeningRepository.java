package com.sesac.backend.course.repository;

import com.sesac.backend.course.constant.CourseStatus;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseOpeningRepository extends JpaRepository<CourseOpening, UUID> {
    // 특정 강의의 개설 목록 조회
    List<CourseOpening> findByCourse(Course course);

    // 동일 강의의 특정 학기 중복 개설 확인
    boolean existsByCourseAndYearAndSemesterAndProfessorId(
            Course course,
            Integer year,
            String semester,
            String professorId
    );

    // 특정 교수의 특정 학기 강의 목록 조회
    List<CourseOpening> findByProfessorIdAndYearAndSemester(
            String professorId,
            Integer year,
            String semester
    );

    // 특정 상태의 강의 목록 조회
    List<CourseOpening> findByStatus(CourseStatus status);

}