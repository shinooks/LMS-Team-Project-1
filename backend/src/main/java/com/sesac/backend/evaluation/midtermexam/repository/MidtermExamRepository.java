package com.sesac.backend.evaluation.midtermexam.repository;

import com.sesac.backend.evaluation.midtermexam.domain.MidtermExam;
import org.springframework.data.jpa.repository.JpaRepository;
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
