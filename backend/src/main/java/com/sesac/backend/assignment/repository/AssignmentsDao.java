package com.sesac.backend.assignment.repository;

import com.sesac.backend.assignment.domain.AssignmentsDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentsDao extends JpaRepository<AssignmentsDo, Long> {

}
