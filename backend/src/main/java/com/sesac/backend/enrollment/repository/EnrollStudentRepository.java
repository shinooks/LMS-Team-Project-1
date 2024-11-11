package com.sesac.backend.enrollment.repository;

import com.sesac.backend.entity.Student;
import com.sesac.backend.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollStudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUser(UserAuthentication studentId);
}
