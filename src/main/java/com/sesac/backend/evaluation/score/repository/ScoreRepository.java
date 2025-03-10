package com.sesac.backend.evaluation.score.repository;

import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.exam.domain.Exam;
import com.sesac.backend.evaluation.score.domain.Score;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {

    Optional<Score> findByStudentAndCourseOpening(Student student, CourseOpening courseOpening);

    Optional<Score> findByMidtermExam(Exam exam);
    
    Optional<Score> findByFinalExam(Exam exam);
}
