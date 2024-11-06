package com.sesac.backend.course.repository;

import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseOpeningRepository extends JpaRepository<CourseOpening, UUID> {
    List<CourseOpening> findByCourse(Course course);
}
