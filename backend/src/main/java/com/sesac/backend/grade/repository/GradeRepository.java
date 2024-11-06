package com.sesac.backend.grade.repository;

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
    List<Grade> findAllByCourseCourseNameAndCourseOpeningSemester(String courseName, String semester);
}
