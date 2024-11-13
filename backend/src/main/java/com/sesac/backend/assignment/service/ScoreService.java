package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.Assignment;
import com.sesac.backend.assignment.domain.FinalExam;
import com.sesac.backend.assignment.domain.MidtermExam;
import com.sesac.backend.assignment.domain.Score;
import com.sesac.backend.assignment.dto.AssignmentDto;
import com.sesac.backend.assignment.dto.ScoreDto;
import com.sesac.backend.assignment.repository.*;

import java.util.*;

import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final MidtermExamRepository midtermExamRepository;
    private final FinalExamRepository finalExamRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepositoryDemo studentRepository;
    private final CourseOpeningRepository courseOpeningRepository;

    @Autowired
    public ScoreService(ScoreRepository scoreRepository, MidtermExamRepository midtermExamRepository, FinalExamRepository finalExamRepository, AssignmentRepository assignmentRepository, StudentRepositoryDemo studentRepository, CourseOpeningRepository courseOpeningRepository) {
        this.scoreRepository = scoreRepository;
        this.midtermExamRepository = midtermExamRepository;
        this.finalExamRepository = finalExamRepository;
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
        this.courseOpeningRepository = courseOpeningRepository;
    }

    public ScoreDto findById(UUID scoreId) {
        return scoreRepository.findById(scoreId).stream().map(this::convertToDto).findFirst().orElseThrow(RuntimeException::new);
    }

    public List<ScoreDto> findAll() {
        return scoreRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public ScoreDto createScore(ScoreDto scoreDto) {
        return convertToDto(scoreRepository.save(convertToEntity(scoreDto)));
    }

    //점수 수정
    public ScoreDto updateScore(ScoreDto scoreDto) {
        Score score = scoreRepository.findById(scoreDto.getScoreId()).orElseThrow(RuntimeException::new);
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

    //엔티티를 dto로
    private ScoreDto convertToDto(Score entity) {
        return new ScoreDto(entity.getScoreId(), entity.getAssignment().getAssignId(), entity.getMidtermExam().getMidtermExamId(), entity.getFinalExam().getFinalExamId(), entity.getCourseOpening().getOpeningId(), entity.getStudent().getStudentId(), entity.getAssignScore(), entity.getMidtermExamScore(), entity.getFinalExamScore(), entity.getVisibility());
    }

    // dto 를 엔티티로
    private Score convertToEntity(ScoreDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignId()).orElse(null);
        MidtermExam midtermExam = midtermExamRepository.findById(dto.getMidtermExamId()).orElse(null);
        FinalExam finalExam = finalExamRepository.findById(dto.getFinalExamId()).orElse(null);
        Student student = studentRepository.findById(dto.getStudentId()).orElse(null);
        CourseOpening courseOpening = courseOpeningRepository.findById(dto.getOpeningId()).orElse(null);

        return new Score(dto.getScoreId(), assignment, midtermExam, finalExam, courseOpening, student, dto.getAssignScore(), dto.getMidtermExamScore(), dto.getFinalExamScore(), dto.getVisibility());
    }
}
