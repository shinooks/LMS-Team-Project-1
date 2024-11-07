package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.MidtermExam;
import com.sesac.backend.assignment.dto.MidtermExamDto;
import com.sesac.backend.assignment.repository.MidtermExamDao;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * @author dongjin
 * 중간고사 service
 * MidtermExam 비즈니스 로직 구현
 */
@Service
public class MidtermExamService {

    private final MidtermExamDao midtermExamDao;

    public MidtermExamService(MidtermExamDao midtermExamDao) {
        this.midtermExamDao = midtermExamDao;
    }

    /**
     * MidtermExam 테이블 레코드 midtermExamId(PK)로 조회
     * @param midtermExamId
     * @return MidtermExamDto
     */
    public MidtermExamDto getMidtermExam(UUID midtermExamId) {
        return midtermExamDao.findById(midtermExamId).stream()
            .map(this::convertToDto)
            .findFirst().orElseThrow(RuntimeException::new);
    }

    /**
     * MidtermExam 테이블 레코드 전체 조회
     * @return List<MidtermExamDto>
     */
    public List<MidtermExamDto> getAllMidtermExams() {
        return midtermExamDao.findAll().stream().map(this::convertToDto).toList();
    }

    /**
     * MidtermExam 테이블 레코드 생성
     * @param midtermExamDto
     * @return MidtermExamDto
     */
    public MidtermExamDto createMidtermExam(MidtermExamDto midtermExamDto) {
        return convertToDto(midtermExamDao.save(convertToEntity(midtermExamDto)));
    }

    /**
     * MidtermExam 테이블 레코드 수정
     * @param midtermExamDto
     * @return MidtermExamDto
     */
    public MidtermExamDto updateMidtermExam(MidtermExamDto midtermExamDto) {
        MidtermExam saved = midtermExamDao.findById(midtermExamDto.getMidtermExamId())
            .orElseThrow(RuntimeException::new);

        saved.setStartTime(midtermExamDto.getStartTime());
        saved.setEndTime(midtermExamDto.getEndTime());

        return convertToDto(midtermExamDao.save(saved));
    }

    /**
     * MidtermExam 테이블 레코드 midtermExamId(PK)로 삭제
     * @param midtermExamId
     */
    public void deleteMidtermExam(UUID midtermExamId) {
        midtermExamDao.findById(midtermExamId).orElseThrow(RuntimeException::new);
        midtermExamDao.deleteById(midtermExamId);
    }

    /**
     * MidtermExam entity를 MidtermExam dto로 변환
     * @param entity
     * @return MidtermExamDto
     */
    private MidtermExamDto convertToDto(MidtermExam entity) {
        return new MidtermExamDto(entity.getMidtermExamId(), entity.getCourse(),
            entity.getStartTime(), entity.getEndTime());
    }

    /**
     * MidtermExam dto를 MidtermExam entity로 변환
     * @param dto
     * @return MidtermExam
     */
    private MidtermExam convertToEntity(MidtermExamDto dto) {
        return new MidtermExam(dto.getMidtermExamId(), dto.getCourse(), dto.getStartTime(),
            dto.getEndTime());
    }
}
