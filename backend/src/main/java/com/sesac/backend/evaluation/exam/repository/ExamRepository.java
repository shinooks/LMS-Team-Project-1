package com.sesac.backend.evaluation.exam.repository;

import com.sesac.backend.evaluation.exam.domain.Exam;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dongjin
 * 시험 Repository
 * Exam 테이블 연결
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    List<Exam> findAllByCourseOpeningOpeningIdAndStudentStudentId(UUID openingId, UUID studentId);
}
