package com.sesac.backend.course.repository;

import com.sesac.backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    // 기본 CRUD 메서드는 JpaRepository에서 제공
}
