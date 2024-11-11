package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.FinalExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FinalExamRepository extends JpaRepository<FinalExam, UUID> {
}
