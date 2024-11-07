package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.FinalExamScore;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dongjin
 * 기말고사 점수 Repository
 * FinalExamScore 테이블 연결
 */
@Repository
public interface FinalExamScoreDao extends JpaRepository<FinalExamScore, UUID> {

}
