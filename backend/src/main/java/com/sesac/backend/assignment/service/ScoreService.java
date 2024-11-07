package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.Score;
import com.sesac.backend.assignment.dto.ScoreDto;
import com.sesac.backend.assignment.repository.ScoreDao;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    private final ScoreDao scoreDao;

    @Autowired
    public ScoreService(ScoreDao scoreDao) {
        this.scoreDao = scoreDao;
    }

    public ScoreDto findById(UUID scoreId) {
        return scoreDao.findById(scoreId).stream().map(this::convertToDto).findFirst().orElseThrow(RuntimeException::new);
    }

    public List<ScoreDto> findAll() {
        return scoreDao.findAll().stream().map(this::convertToDto).toList();
    }

    public ScoreDto createScore(ScoreDto scoreDto) {
        return convertToDto(scoreDao.save(convertToEntity(scoreDto)));
    }

    public ScoreDto updateScore(ScoreDto scoreDto) {
        Score score = scoreDao.findById(scoreDto.getScoreId()).orElseThrow(RuntimeException::new);
        score.setAssignScore(scoreDto.getAssignScore());
        score.setMidtermExamScore(scoreDto.getMidtermExamScore());
        score.setFinalExamScore(scoreDto.getFinalExamScore());
        score.setVisibility(scoreDto.getVisibility());
        return convertToDto(scoreDao.save(score));
    }

    public void deleteScore(UUID scoreId) {
        scoreDao.findById(scoreId).orElseThrow(RuntimeException::new);
        scoreDao.deleteById(scoreId);
    }

    private ScoreDto convertToDto(Score entity) {
        return new ScoreDto(entity.getScoreId(), entity.getAssignment(), entity.getMidtermExam(), entity.getFinalExam(), entity.getStudent(), entity.getAssignScore(), entity.getMidtermExamScore(), entity.getFinalExamScore(), entity.getVisibility());
    }

    private Score convertToEntity(ScoreDto dto) {
        return new Score(dto.getScoreId(), dto.getAssignment(), dto.getMidtermExam(), dto.getFinalExam(), dto.getStudent(), dto.getAssignScore(), dto.getMidtermExamScore(), dto.getFinalExamScore(), dto.getVisibility());
    }
}
