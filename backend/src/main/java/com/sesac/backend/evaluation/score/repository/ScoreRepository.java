package com.sesac.backend.evaluation.score.repository;

import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.exam.domain.Exam;
import com.sesac.backend.evaluation.score.domain.Score;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {

    Optional<Score> findByStudent(Student student);
}
