package com.sesac.backend.evaluation.assignment.repository;

import com.sesac.backend.evaluation.assignment.domain.AssignSubmit;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author dongjin
 * 과제 제출 Repository
 * AssignSubmit 테이블 연결
 */
@Repository
public interface AssignSubmitRepository extends JpaRepository<AssignSubmit, UUID> {

    AssignSubmit findByAssignmentAssignId(UUID assignId);

}
