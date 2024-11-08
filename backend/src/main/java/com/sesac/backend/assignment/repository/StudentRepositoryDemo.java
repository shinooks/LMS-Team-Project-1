package com.sesac.backend.assignment.repository;

import com.sesac.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepositoryDemo extends JpaRepository<Student, UUID> {
}
