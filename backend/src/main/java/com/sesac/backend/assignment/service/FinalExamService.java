package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.FinalExam;
import com.sesac.backend.assignment.dto.FinalExamDto;
import com.sesac.backend.assignment.repository.FinalExamDao;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin
 * 기말고사 service
 * FinalExam 비즈니스 로직 구현
 */
@Service
public class FinalExamService {

    private final FinalExamDao finalExamDao;

    @Autowired
    public FinalExamService(FinalExamDao finalExamDao) {
        this.finalExamDao = finalExamDao;
    }

    /**
     * FinalExam 테이블 레코드 finalExamId(PK)로 조회
     * @param finalExamId
     * @return FinalExamDto
     */
    public FinalExamDto getByExamId(UUID finalExamId) {
        return finalExamDao.findById(finalExamId).stream()
            .map(this::convertToDto).findFirst()
            .orElseThrow(RuntimeException::new);
    }

    /**
     * FinalExam 테이블 레코드 전체조회
     * @return List<FinalExamDto>
     */
    public List<FinalExamDto> getAllFinalExams() {
        return finalExamDao.findAll().stream().map(this::convertToDto).toList();
    }

    /**
     * FinalExam 테이블 레코드 생성
     * @param finalExamDto
     * @return FinalExamDto
     */
    public FinalExamDto createFinalExam(FinalExamDto finalExamDto) {
        return convertToDto(finalExamDao.save(convertToEntity(finalExamDto)));
    }

    /**
     * FinalExam 테이블 레코드 업데이트
     * @param finalExamDto
     * @return FinalExamDto
     */
    public FinalExamDto updateFinalExam(FinalExamDto finalExamDto) {
        FinalExam saved = finalExamDao.findById(finalExamDto.getFinalExamId())
            .orElseThrow(RuntimeException::new);
        saved.setStartTime(finalExamDto.getStartTime());
        saved.setEndTime(finalExamDto.getEndTime());
        return convertToDto(finalExamDao.save(saved));
    }

    /**
     * FinalExam 테이블 레코드 finalExamId(PK)로 삭제
     * @param finalExamId
     */
    public void deleteFinalExam(UUID finalExamId) {
        finalExamDao.findById(finalExamId).orElseThrow(RuntimeException::new);
        finalExamDao.deleteById(finalExamId);
    }

    /**
     * FinalExam entity를 FinalExam dto로 변환
     * @param entity
     * @return FinalExamDto
     */
    private FinalExamDto convertToDto(FinalExam entity) {
        return new FinalExamDto(entity.getFinalExamId(), entity.getCourse(),
            entity.getStartTime(), entity.getEndTime());
    }

    /**
     * FinalExam dto를 FinalExam entity로 변환
     * @param dto
     * @return FinalExam
     */
    private FinalExam convertToEntity(FinalExamDto dto) {
        return new FinalExam(dto.getFinalExamId(), dto.getCourse(), dto.getStartTime(),
            dto.getEndTime());
    }
}
