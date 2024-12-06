package com.sesac.backend.enrollment.repository;

import com.sesac.backend.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfessorRepositoryTmp extends JpaRepository<Professor, UUID> {
}
