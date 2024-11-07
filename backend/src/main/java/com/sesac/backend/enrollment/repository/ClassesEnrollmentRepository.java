package com.sesac.backend.enrollment.repository;

import com.sesac.backend.enrollment.domain.classEnrollment.CourseEnrollment;
import com.sesac.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface ClassesEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    Set<CourseEnrollment> findByStudentId(UUID studentId);
}
