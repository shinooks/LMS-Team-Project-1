package com.sesac.backend.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sesac.backend.assignment.domain.MidtermExam;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface MidtermExamDao extends JpaRepository<MidtermExam, UUID> {

}
