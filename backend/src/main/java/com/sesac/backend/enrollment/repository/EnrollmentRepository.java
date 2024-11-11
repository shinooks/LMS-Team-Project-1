package com.sesac.backend.enrollment.repository;

import com.sesac.backend.entity.Enrollment;
import com.sesac.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Set<Enrollment> findByStudent(Student studentId);

}
