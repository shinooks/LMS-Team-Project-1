package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.dto.AssignScoreDto;
import com.sesac.backend.assignment.repository.AssignScoreDao;
import com.sesac.backend.assignment.domain.AssignScore;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin
 * 과제 점수 service
 * AssignScore 비즈니스 로직 구현
 */
@Service
public class AssignScoreService {

    private final AssignScoreDao assignScoreDao;

    @Autowired
    public AssignScoreService(AssignScoreDao assignScoreDao) {
        this.assignScoreDao = assignScoreDao;
    }

    /**
     * AssignScore 테이블 레코드 assignScoreId(PK)로 조회
     * @param assignScoreId
     * @return AssignScoreDto
     */
    public AssignScoreDto findById(UUID assignScoreId) {
        AssignScore entity = assignScoreDao.findById(assignScoreId).orElse(null);

        return entity == null ? null
            : new AssignScoreDto(entity.getAssignScoreId(), entity.getAssignment(),
                entity.getStudent(), entity.getScore(), entity.getComment(), entity.getVisibility());
    }

    /**
     * AssignScore 테이블 레코드 전체 조회
     * @return List<AssignScoreDto>
     */
    public List<AssignScoreDto> getAll() {
        return assignScoreDao.findAll().stream().map(
            entity -> new AssignScoreDto(entity.getAssignScoreId(), entity.getAssignment(),
                entity.getStudent(), entity.getScore(), entity.getComment(), entity.getVisibility())).toList();
    }

    /**
     * AssignScore 테이블 레코드 생성, 수정
     * @param assignScoreDto
     * @return AssignScoreDto
     */
    public AssignScoreDto save(AssignScoreDto assignScoreDto) {
        AssignScore entity = assignScoreDao.save(
            new AssignScore(assignScoreDto.getAssignScoreId(), assignScoreDto.getAssignment(),
                assignScoreDto.getStudent(), assignScoreDto.getScore(), assignScoreDto.getComment(),
                assignScoreDto.getVisibility()));

        return new AssignScoreDto(entity.getAssignScoreId(), entity.getAssignment(),
            entity.getStudent(), entity.getScore(), entity.getComment(), entity.getVisibility());
    }

    /**
     * AssignScore 테이블 레코드 assignScoreId(PK)로 삭제
     * @param assignScoreId
     */
    public void delete(UUID assignScoreId) {
        assignScoreDao.deleteById(assignScoreId);
    }
}
