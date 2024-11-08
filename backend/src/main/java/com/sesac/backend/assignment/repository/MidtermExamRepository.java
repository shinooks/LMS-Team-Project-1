package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.MidtermExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MidtermExamRepository extends JpaRepository<MidtermExam, UUID> {
}
