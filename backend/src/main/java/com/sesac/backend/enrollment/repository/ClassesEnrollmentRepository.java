package com.sesac.backend.enrollment.repository;

import com.sesac.backend.enrollment.domain.classEnrollment.CourseEnrollment;
import com.sesac.backend.entity.Course;
import com.sesac.backend.entity.Student;
import com.sesac.backend.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.javapoet.ClassName;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ClassesEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    Set<CourseEnrollment> findByStudent(Student studentId);
}
