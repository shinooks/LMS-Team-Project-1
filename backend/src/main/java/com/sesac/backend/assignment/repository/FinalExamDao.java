package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.FinalExam;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalExamDao extends JpaRepository<FinalExam, UUID> {

}
