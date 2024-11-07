package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.MidtermExamScore;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MidtermExamScoreDao extends JpaRepository<MidtermExamScore, UUID> {

}
