package com.sesac.backend.evaluation.score.service;

import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.assignment.repository.AssignmentRepository;
import com.sesac.backend.evaluation.assignment.repository.StudentRepositoryDemo;
import com.sesac.backend.evaluation.finalexam.domain.FinalExam;
import com.sesac.backend.evaluation.finalexam.repository.FinalExamRepository;
import com.sesac.backend.evaluation.midtermexam.domain.MidtermExam;
import com.sesac.backend.evaluation.midtermexam.repository.MidtermExamRepository;
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
    private final MidtermExamRepository midtermExamRepository;
    private final FinalExamRepository finalExamRepository;
    private final StudentRepositoryDemo studentRepositoryDemo;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository, AssignmentRepository assignmentRepository,
        MidtermExamRepository midtermExamRepository, FinalExamRepository finalExamRepository,
        StudentRepositoryDemo studentRepositoryDemo) {
        this.scoreRepository = scoreRepository;
        this.assignmentRepository = assignmentRepository;
        this.midtermExamRepository = midtermExamRepository;
        this.finalExamRepository = finalExamRepository;
        this.studentRepositoryDemo = studentRepositoryDemo;
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
            entity.getMidtermExam().getMidtermExamId(), entity.getFinalExam().getFinalExamId(),
            entity.getStudent().getStudentId(), entity.getAssignScore(),
            entity.getMidtermExamScore(), entity.getFinalExamScore(), entity.getVisibility());
    }

    private Score convertToEntity(ScoreDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignId())
            .orElseThrow(RuntimeException::new);
        MidtermExam midtermExam = midtermExamRepository.findById(dto.getMidtermExamId())
            .orElseThrow(RuntimeException::new);
        FinalExam finalExam = finalExamRepository.findById(dto.getFinalExamId())
            .orElseThrow(RuntimeException::new);
        Student student = studentRepositoryDemo.findById(dto.getStudentId())
            .orElseThrow(RuntimeException::new);

        return new Score(dto.getScoreId(), assignment, midtermExam, finalExam, student,
            dto.getAssignScore(), dto.getMidtermExamScore(), dto.getFinalExamScore(),
            dto.getVisibility());
    }
}
