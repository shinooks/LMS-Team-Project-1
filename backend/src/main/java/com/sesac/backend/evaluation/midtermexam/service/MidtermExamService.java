package com.sesac.backend.evaluation.midtermexam.service;

import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
import com.sesac.backend.evaluation.midtermexam.domain.MidtermExam;
import com.sesac.backend.evaluation.midtermexam.dto.MidtermExamDto;
import com.sesac.backend.evaluation.midtermexam.repository.MidtermExamRepository;
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

    private final MidtermExamRepository midtermExamRepository;
    private final CourseRepository courseRepository;

    public MidtermExamService(MidtermExamRepository midtermExamRepository,
        CourseRepository courseRepository) {
        this.midtermExamRepository = midtermExamRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * MidtermExam 테이블 레코드 midtermExamId(PK)로 조회
     * @param midtermExamId
     * @return MidtermExamDto
     */
    public MidtermExamDto getMidtermExam(UUID midtermExamId) {
        return midtermExamRepository.findById(midtermExamId).stream()
            .map(this::convertToDto)
            .findFirst().orElseThrow(RuntimeException::new);
    }

    /**
     * MidtermExam 테이블 레코드 전체 조회
     * @return List<MidtermExamDto>
     */
    public List<MidtermExamDto> getAllMidtermExams() {
        return midtermExamRepository.findAll().stream().map(this::convertToDto).toList();
    }

    /**
     * MidtermExam 테이블 레코드 생성
     * @param midtermExamDto
     * @return MidtermExamDto
     */
    public MidtermExamDto createMidtermExam(MidtermExamDto midtermExamDto) {
        return convertToDto(midtermExamRepository.save(convertToEntity(midtermExamDto)));
    }

    /**
     * MidtermExam 테이블 레코드 수정
     * @param midtermExamDto
     * @return MidtermExamDto
     */
    public MidtermExamDto updateMidtermExam(MidtermExamDto midtermExamDto) {
        MidtermExam saved = midtermExamRepository.findById(midtermExamDto.getMidtermExamId())
            .orElseThrow(RuntimeException::new);

        saved.setStartTime(midtermExamDto.getStartTime());
        saved.setEndTime(midtermExamDto.getEndTime());

        return convertToDto(midtermExamRepository.save(saved));
    }

    /**
     * MidtermExam 테이블 레코드 midtermExamId(PK)로 삭제
     * @param midtermExamId
     */
    public void deleteMidtermExam(UUID midtermExamId) {
        midtermExamRepository.findById(midtermExamId).orElseThrow(RuntimeException::new);
        midtermExamRepository.deleteById(midtermExamId);
    }

    /**
     * MidtermExam entity를 MidtermExam dto로 변환
     * @param entity
     * @return MidtermExamDto
     */
    private MidtermExamDto convertToDto(MidtermExam entity) {

        return new MidtermExamDto(entity.getMidtermExamId(), entity.getCourse().getCourseId(),
            entity.getStartTime(), entity.getEndTime());
    }

    /**
     * MidtermExam dto를 MidtermExam entity로 변환
     * @param dto
     * @return MidtermExam
     */
    private MidtermExam convertToEntity(MidtermExamDto dto) {
        Course course = courseRepository.findById(dto.getCourseId()).orElseThrow(RuntimeException::new);

        return new MidtermExam(dto.getMidtermExamId(), course, dto.getStartTime(),
            dto.getEndTime());
    }
}
