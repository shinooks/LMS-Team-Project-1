package com.sesac.backend.evaluation.score.service;

import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.assignment.repository.AssignmentRepository;
import com.sesac.backend.evaluation.assignment.repository.StudentRepositoryDemo;
import com.sesac.backend.evaluation.exam.domain.Exam;
import com.sesac.backend.evaluation.exam.repository.ExamRepository;
import com.sesac.backend.evaluation.score.domain.Score;
import com.sesac.backend.evaluation.score.dto.ScoreDto;
import com.sesac.backend.evaluation.score.repository.ScoreRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final AssignmentRepository assignmentRepository;
    private final ExamRepository examRepository;
    private final StudentRepositoryDemo studentRepositoryDemo;
    private final CourseOpeningRepository courseOpeningRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository, AssignmentRepository assignmentRepository,
        ExamRepository examRepository,
        StudentRepositoryDemo studentRepositoryDemo,
        CourseOpeningRepository courseOpeningRepository) {
        this.scoreRepository = scoreRepository;
        this.assignmentRepository = assignmentRepository;
        this.examRepository = examRepository;
        this.studentRepositoryDemo = studentRepositoryDemo;
        this.courseOpeningRepository = courseOpeningRepository;
    }

    public ScoreDto findById(UUID scoreId) {
        return scoreRepository.findById(scoreId).stream().map(this::convertToDto).findFirst()
            .orElseThrow(RuntimeException::new);
    }

    public List<ScoreDto> findAll() {
        return scoreRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public ScoreDto createScore(ScoreDto scoreDto) {
        return convertToDto(scoreRepository.save(convertToEntity(scoreDto)));
    }

    public ScoreDto updateScore(ScoreDto scoreDto) {
        Score score = scoreRepository.findById(scoreDto.getScoreId())
            .orElseThrow(RuntimeException::new);
        score.setAssignScore(scoreDto.getAssignScore());
        score.setMidtermExamScore(scoreDto.getMidtermExamScore());
        score.setFinalExamScore(scoreDto.getFinalExamScore());
        score.setVisibility(scoreDto.getVisibility());
        return convertToDto(scoreRepository.save(score));
    }

    public void deleteScore(UUID scoreId) {
        scoreRepository.findById(scoreId).orElseThrow(RuntimeException::new);
        scoreRepository.deleteById(scoreId);
    }

    private ScoreDto convertToDto(Score entity) {
        return new ScoreDto(entity.getScoreId(), entity.getAssignment().getAssignId(),
            entity.getMidtermExam().getExamId(), entity.getFinalExam().getExamId(),
            entity.getCourseOpening().getOpeningId(),
            entity.getStudent().getStudentId(), entity.getAssignScore(),
            entity.getMidtermExamScore(), entity.getFinalExamScore(), entity.getVisibility());
    }

    private Score convertToEntity(ScoreDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignId())
            .orElseThrow(RuntimeException::new);
        Exam midtermExam = examRepository.findById(dto.getMidtermExamId())
            .orElseThrow(RuntimeException::new);
        Exam finalExam = examRepository.findById(dto.getFinalExamId())
            .orElseThrow(RuntimeException::new);
        Student student = studentRepositoryDemo.findById(dto.getStudentId())
            .orElseThrow(RuntimeException::new);
        CourseOpening courseOpening = courseOpeningRepository.findById(dto.getOpeningId())
            .orElseThrow(RuntimeException::new);

        return new Score(dto.getScoreId(), assignment, midtermExam, finalExam, courseOpening,
            student, dto.getAssignScore(), dto.getMidtermExamScore(), dto.getFinalExamScore(),
            dto.getVisibility());
    }
}
