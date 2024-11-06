package com.sesac.backend.enrollment.repository;

import com.sesac.backend.enrollment.domain.classEnrollment.ClassEnrollment;
import com.sesac.backend.enrollment.dto.ClassesDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ClassesEnrollmentRepository extends JpaRepository<ClassEnrollment, Long> {
    Set<ClassEnrollment> findByStudentId(String studentId);
}
