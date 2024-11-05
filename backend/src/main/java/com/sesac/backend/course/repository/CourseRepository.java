package com.sesac.backend.course.repository;

import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    long countByDepartment(Department department);
}
