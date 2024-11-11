package com.sesac.backend.evaluation.assignment.repository;

import com.sesac.backend.evaluation.assignment.domain.Assignment;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dongjin
 * 과제 Repository
 * Assignment 테이블 연결
 */
@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

}
