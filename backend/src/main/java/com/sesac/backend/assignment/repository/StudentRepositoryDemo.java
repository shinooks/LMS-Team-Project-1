package com.sesac.backend.assignment.repository;

import com.sesac.backend.entity.Student;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepositoryDemo extends JpaRepository<Student, UUID> {

}
