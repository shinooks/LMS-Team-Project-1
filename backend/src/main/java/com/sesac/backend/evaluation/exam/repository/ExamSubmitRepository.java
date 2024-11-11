package com.sesac.backend.evaluation.exam.repository;

import com.sesac.backend.evaluation.exam.domain.ExamSubmit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSubmitRepository extends JpaRepository<ExamSubmit, UUID> {

}
