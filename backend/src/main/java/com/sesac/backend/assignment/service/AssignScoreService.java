package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.dto.AssignScoreDto;
import com.sesac.backend.assignment.repository.AssignScoreDao;
import com.sesac.backend.assignment.domain.AssignScore;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignScoreService {

    private final AssignScoreDao assignScoreDao;

    @Autowired
    public AssignScoreService(AssignScoreDao assignScoreDao) {
        this.assignScoreDao = assignScoreDao;
    }

    public AssignScoreDto findById(UUID assignScoreId) {
        AssignScore entity = assignScoreDao.findById(assignScoreId).orElse(null);

        return entity == null ? null
            : new AssignScoreDto(entity.getAssignScoreId(), entity.getAssignment(),
                entity.getStudent(), entity.getScore(), entity.getComment(), entity.getVisibility());
    }

    public List<AssignScoreDto> getAll() {
        return assignScoreDao.findAll().stream().map(
            entity -> new AssignScoreDto(entity.getAssignScoreId(), entity.getAssignment(),
                entity.getStudent(), entity.getScore(), entity.getComment(), entity.getVisibility())).toList();
    }

    public AssignScoreDto save(AssignScoreDto assignScoreDto) {
        AssignScore entity = assignScoreDao.save(
            new AssignScore(assignScoreDto.getAssignScoreId(), assignScoreDto.getAssignment(),
                assignScoreDto.getStudent(), assignScoreDto.getScore(), assignScoreDto.getComment(),
                assignScoreDto.getVisibility()));

        return new AssignScoreDto(entity.getAssignScoreId(), entity.getAssignment(),
            entity.getStudent(), entity.getScore(), entity.getComment(), entity.getVisibility());
    }

    public void delete(UUID assignScoreId) {
        assignScoreDao.deleteById(assignScoreId);
    }
}
