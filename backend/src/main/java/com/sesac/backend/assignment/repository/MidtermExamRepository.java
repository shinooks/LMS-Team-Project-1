package com.sesac.backend.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sesac.backend.assignment.domain.MidtermExam;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * @author dongjin
 * 중간고사 Repository
 * MidtermExam 테이블 연결
 */
@Repository
public interface MidtermExamRepository extends JpaRepository<MidtermExam, UUID> {

}
