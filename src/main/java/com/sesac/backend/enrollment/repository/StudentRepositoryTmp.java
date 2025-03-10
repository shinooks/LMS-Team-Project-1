package com.sesac.backend.enrollment.repository;

import com.sesac.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepositoryTmp extends JpaRepository<Student, UUID> {
}
