package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.AssignSubmit;
import com.sesac.backend.assignment.dto.AssignSubmitDto;
import com.sesac.backend.assignment.repository.AssignSubmitDao;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin
 * 과제 제출 service
 * AssignSubmit 비즈니스 로직 구현
 */
@Service
public class AssignSubmitService {

    private final AssignSubmitDao assignSubmitDao;

    @Autowired
    public AssignSubmitService(AssignSubmitDao assignSubmitDao) {
        this.assignSubmitDao = assignSubmitDao;
    }

    /**
     * AssignSubmit 테이블 레코드 assignSubmitId(PK)로 조회
     * @param assignSubmitId
     * @return AssignSubmitDto
     */
    public AssignSubmitDto findById(UUID assignSubmitId) {
        AssignSubmit entity = assignSubmitDao.findById(assignSubmitId).orElse(null);
        return entity == null ? null
            : new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
                entity.getStudent(), entity.getAnswer(), entity.getSubmitAt(), entity.getFileName());
    }

    /**
     * AssignSubmit 테이블 레코드 전체 조회
     * @return List<AssignSubmitDto>
     */
    public List<AssignSubmitDto> getAll() {
        return assignSubmitDao.findAll().stream().map(
            entity -> new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
                entity.getStudent(), entity.getAnswer(), entity.getSubmitAt(), entity.getFileName())).toList();
    }

    /**
     * AssignSubmit 테이블 레코드 생성 (과제 제출)
     * AssignSubmit 테이블의 제출일시, Assignment 테이블의 제출기한을 비교해서
     * 제출일시가 제출기한 이전이면 제출 성공, 제출일시가 제출기한 이후면 제출 실패
     * @param assignSubmitDto
     * @return 성공 true, 실패 false
     */
    public boolean submit(AssignSubmitDto assignSubmitDto) {
        LocalDateTime now = assignSubmitDto.getSubmitAt();
        LocalDateTime deadline = assignSubmitDto.getAssignment().getDeadline();

        boolean flag = false;

        if (now.isBefore(deadline)) {
            assignSubmitDao.save(
                new AssignSubmit(assignSubmitDto.getAssignSubmitId(),
                    assignSubmitDto.getAssignment(), assignSubmitDto.getStudent(),
                    assignSubmitDto.getAnswer(), now, assignSubmitDto.getFileName()));

            flag = true;
            return flag;
        }

        return flag;
    }

    /**
     * AssignSubmit 테이블 레코드 수정
     * @param assignSubmitDto
     * @return AssignSubmitDto
     */
    public AssignSubmitDto update(AssignSubmitDto assignSubmitDto) {
        AssignSubmit entity = assignSubmitDao.save(
            new AssignSubmit(assignSubmitDto.getAssignSubmitId(), assignSubmitDto.getAssignment(),
                assignSubmitDto.getStudent(), assignSubmitDto.getAnswer(),
                assignSubmitDto.getSubmitAt(), assignSubmitDto.getFileName()));

        return new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
            entity.getStudent(), entity.getAnswer(), entity.getSubmitAt(), entity.getFileName());
    }

    /**
     * AssignSubmit 테이블 레코드 assignSubmitId(PK)로 삭제
     * @param assignSubmitId
     */
    public void delete(UUID assignSubmitId) {
        assignSubmitDao.deleteById(assignSubmitId);
    }
}
