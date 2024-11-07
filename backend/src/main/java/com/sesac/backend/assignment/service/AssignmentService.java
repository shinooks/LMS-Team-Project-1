package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.Assignment;
import com.sesac.backend.assignment.dto.AssignmentDto;
import com.sesac.backend.assignment.repository.AssignmentDao;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin
 * 과제 service
 * Assignment 비즈니스 로직 구현
 */
@Service
public class AssignmentService {

    private final AssignmentDao assignmentDao;

    @Autowired
    public AssignmentService(AssignmentDao assignmentDao) {
        this.assignmentDao = assignmentDao;
    }

    /**
     * Assignment 테이블 레코드 생성, 수정
     * @param assignmentDto
     * @return AssignmentDto
     */
    public AssignmentDto save(AssignmentDto assignmentDto) {
        Assignment assignment = assignmentDao.save(
            new Assignment(assignmentDto.getAssignId(), assignmentDto.getCourse(),
                assignmentDto.getTitle(), assignmentDto.getDescription(),
                assignmentDto.getDeadline()));

        return new AssignmentDto(assignment.getAssignId(), assignment.getCourse(),
            assignment.getTitle(), assignment.getDescription(), assignment.getDeadline());
    }

    /**
     * Assignment 테이블 전체조회
     * @return List<AssignmentDto>
     */
    public List<AssignmentDto> getAll() {
        return assignmentDao.findAll().stream()
            .map(entity -> new AssignmentDto(entity.getAssignId(),
                entity.getCourse(), entity.getTitle(), entity.getDescription(),
                entity.getDeadline())).toList();
    }

    /**
     * Assignment 테이블 레코드 assignId(PK)로 조회
     * @param assignId
     * @return AssignmentDto
     */
    public AssignmentDto findById(UUID assignId) {
        Assignment entity = assignmentDao.findById(assignId).orElse(null);

        return entity == null ? null
            : new AssignmentDto(entity.getAssignId(), entity.getCourse(), entity.getTitle(),
                entity.getDescription(), entity.getDeadline());
    }

    /**
     * Assignment 테이블 레코드 assignId(PK)로 삭제
     * @param assignId
     */
    public void delete(UUID assignId) {
        assignmentDao.deleteById(assignId);
    }
}
