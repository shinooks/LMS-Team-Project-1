package com.sesac.backend.grade.repository;

import com.sesac.backend.grade.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 성적 정보 레포지토리
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    // 단일 파라미터로 수정
    List<Grade> findByAssignmentDomeId(Long assignmentId);

    // 필요한 경우 추가
    Optional<Grade> findByIdAndAssignmentDomeId(Long gradeId, Long assignmentId);
}