package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.FinalExam;
import com.sesac.backend.assignment.dto.FinalExamDto;
import com.sesac.backend.assignment.repository.FinalExamRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
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

    private final FinalExamRepository finalExamRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public FinalExamService(FinalExamRepository finalExamRepository,
        CourseRepository courseRepository) {
        this.finalExamRepository = finalExamRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * FinalExam 테이블 레코드 finalExamId(PK)로 조회
     * @param finalExamId
     * @return FinalExamDto
     */
    public FinalExamDto getByExamId(UUID finalExamId) {
        return finalExamRepository.findById(finalExamId).stream()
            .map(this::convertToDto).findFirst()
            .orElseThrow(RuntimeException::new);
    }

    /**
     * FinalExam 테이블 레코드 전체조회
     * @return List<FinalExamDto>
     */
    public List<FinalExamDto> getAllFinalExams() {
        return finalExamRepository.findAll().stream().map(this::convertToDto).toList();
    }

    /**
     * FinalExam 테이블 레코드 생성
     * @param finalExamDto
     * @return FinalExamDto
     */
    public FinalExamDto createFinalExam(FinalExamDto finalExamDto) {
        return convertToDto(finalExamRepository.save(convertToEntity(finalExamDto)));
    }

    /**
     * FinalExam 테이블 레코드 업데이트
     * @param finalExamDto
     * @return FinalExamDto
     */
    public FinalExamDto updateFinalExam(FinalExamDto finalExamDto) {
        FinalExam saved = finalExamRepository.findById(finalExamDto.getFinalExamId())
            .orElseThrow(RuntimeException::new);
        saved.setStartTime(finalExamDto.getStartTime());
        saved.setEndTime(finalExamDto.getEndTime());
        return convertToDto(finalExamRepository.save(saved));
    }

    /**
     * FinalExam 테이블 레코드 finalExamId(PK)로 삭제
     * @param finalExamId
     */
    public void deleteFinalExam(UUID finalExamId) {
        finalExamRepository.findById(finalExamId).orElseThrow(RuntimeException::new);
        finalExamRepository.deleteById(finalExamId);
    }

    /**
     * FinalExam entity를 FinalExam dto로 변환
     * @param entity
     * @return FinalExamDto
     */
    private FinalExamDto convertToDto(FinalExam entity) {
        return new FinalExamDto(entity.getFinalExamId(), entity.getCourse().getCourseId(),
            entity.getStartTime(), entity.getEndTime());
    }

    /**
     * FinalExam dto를 FinalExam entity로 변환
     * @param dto
     * @return FinalExam
     */
    private FinalExam convertToEntity(FinalExamDto dto) {
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(RuntimeException::new);

        return new FinalExam(dto.getFinalExamId(), course, dto.getStartTime(),
            dto.getEndTime());
    }
}
