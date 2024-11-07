package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.AssignScore;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dongjin
 * 과제 점수 Repository
 * AssignmentScore 테이블 연결
 */
@Repository
public interface AssignScoreDao extends JpaRepository<AssignScore, UUID> {

}
