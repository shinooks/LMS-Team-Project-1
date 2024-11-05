package com.sesac.backend.assignment.repository;

import com.sesac.backend.entity.AssignScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignScoreDao extends JpaRepository<AssignScore, Integer> {

}
