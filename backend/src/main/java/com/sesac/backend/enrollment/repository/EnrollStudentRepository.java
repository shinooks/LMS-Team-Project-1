package com.sesac.backend.enrollment.repository;

import com.sesac.backend.entity.Enrollment;
import com.sesac.backend.entity.Student;
import com.sesac.backend.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollStudentRepository extends JpaRepository<Student, UUID> {
    //Optional<Student> findByUser(UserAuthentication studentId);
    Optional<Student> findByStudentId(UUID studentId);
}
