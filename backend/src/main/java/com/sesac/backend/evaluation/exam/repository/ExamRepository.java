package com.sesac.backend.evaluation.exam.repository;

import com.sesac.backend.evaluation.exam.domain.Exam;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dongjin
 * 기말고사 Repository
 * FinalExam 테이블 연결
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {

}
