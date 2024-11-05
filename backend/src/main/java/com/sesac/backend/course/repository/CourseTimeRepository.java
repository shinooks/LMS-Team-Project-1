package com.sesac.backend.course.repository;

import com.sesac.backend.entity.CourseTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseTimeRepository  extends JpaRepository<CourseTime, UUID> {
}
