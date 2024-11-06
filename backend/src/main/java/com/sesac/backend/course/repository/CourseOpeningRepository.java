package com.sesac.backend.course.repository;

import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.CourseOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseOpeningRepository extends JpaRepository<CourseOpening, UUID> {
    List<CourseOpening> findByCourse(Course course);
}
