package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.Assignment;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dongjin
 * 과제 Repository
 * Assignment 테이블 연결
 */
@Repository
public interface AssignmentDao extends JpaRepository<Assignment, UUID> {

}
