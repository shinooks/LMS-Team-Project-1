package com.sesac.backend.evaluation.finalexam.repository;

import com.sesac.backend.evaluation.finalexam.domain.FinalExamSubmit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalExamSubmitRepository extends JpaRepository<FinalExamSubmit, UUID> {

}
