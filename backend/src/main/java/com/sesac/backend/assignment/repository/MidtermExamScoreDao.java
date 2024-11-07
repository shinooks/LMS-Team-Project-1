package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.MidtermExamScore;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dongjin
 * 중간고사 점수 Repository
 * MidtermExamScore 테이블 연결
 */
@Repository
public interface MidtermExamScoreDao extends JpaRepository<MidtermExamScore, UUID> {

}
