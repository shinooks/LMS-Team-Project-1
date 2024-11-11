package com.sesac.backend.course.repository;

import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    // 특정 학과의 전체 강의 수 조회
    long countByDepartment(Department department);

    // 특정 학과의 모든 강의 목록 조회
    List<Course> findByDepartment(Department department);

    // 특정 학점의 강의 목록 조회
    List<Course> findByCredits(Integer credits);

    // 강의 코드 중복 확인
    boolean existsByCourseCode(String courseCode);

    // 특정 강의 조회
    List<Course> findCourseByCourseId(UUID courseId);
}