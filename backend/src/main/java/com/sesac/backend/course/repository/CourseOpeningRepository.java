package com.sesac.backend.course.repository;

import com.sesac.backend.course.constant.CourseStatus;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseOpeningRepository extends JpaRepository<CourseOpening, UUID> {
    // 특정 강의의 개설 목록 조회
    List<CourseOpening> findByCourse(Course course);

    // 동일 강의의 특정 학기 중복 개설 확인
    boolean existsByCourseAndYearAndSemesterAndProfessor(
            Course course,
            Integer year,
            String semester,
            Professor professor
    );

    // 특정 교수의 특정 학기 강의 목록 조회
    List<CourseOpening> findByProfessorAndYearAndSemester(
            Professor professor,
            Integer year,
            String semester
    );

    List<CourseOpening> findAllBySemesterAndProfessorAndYearAndCourseCourseId(String semester, Professor professor, int year, UUID courseId);

    // 특정 상태의 강의 목록 조회
    List<CourseOpening> findByStatus(CourseStatus status);

    //gnuke
    @Query("Select co From CourseOpening co JOIN co.courseTimes ct JOIN Course c ON ct.courseOpening = co " +
    "WHERE c.courseId = :courseId")
    List<CourseOpening> findByCourseId(UUID courseId);
}