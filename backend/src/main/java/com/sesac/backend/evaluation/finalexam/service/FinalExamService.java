package com.sesac.backend.evaluation.finalexam.service;

import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
import com.sesac.backend.evaluation.finalexam.domain.FinalExam;
import com.sesac.backend.evaluation.finalexam.domain.FinalExamProblem;
import com.sesac.backend.evaluation.finalexam.dto.FinalExamDto;
import com.sesac.backend.evaluation.finalexam.dto.FinalExamProblemDto;
import com.sesac.backend.evaluation.finalexam.repository.FinalExamProblemRepository;
import com.sesac.backend.evaluation.finalexam.repository.FinalExamRepository;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin 기말고사 service FinalExam 비즈니스 로직 구현
 */
@Service
public class FinalExamService {

    private final FinalExamRepository finalExamRepository;
    private final FinalExamProblemRepository finalExamProblemRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public FinalExamService(FinalExamRepository finalExamRepository,
        FinalExamProblemRepository finalExamProblemRepository, CourseRepository courseRepository) {
        this.finalExamRepository = finalExamRepository;
        this.finalExamProblemRepository = finalExamProblemRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * FinalExam 테이블 레코드 finalExamId(PK)로 조회
     *
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
     *
     * @return List<FinalExamDto>
     */
    public List<FinalExamDto> getAllFinalExams() {
        return finalExamRepository.findAll().stream().map(this::convertToDto).toList();
    }

    /**
     * FinalExam 테이블 레코드 생성
     *
     * @param finalExamDto
     * @return FinalExamDto
     */
    public FinalExamDto createFinalExam(FinalExamDto finalExamDto) {
        return convertToDto(finalExamRepository.save(convertToEntity(finalExamDto)));
    }

    /**
     * FinalExam 테이블 레코드 업데이트
     *
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
     *
     * @param finalExamId
     */
    public void deleteFinalExam(UUID finalExamId) {
        finalExamRepository.findById(finalExamId).orElseThrow(RuntimeException::new);
        finalExamRepository.deleteById(finalExamId);
    }

    /**
     * FinalExam과 1:N 연관된 FinalExamProblems 업데이트
     * 기말고사 시험 문제 출제
     *
     * @param finalExamId
     * @param finalExamProblemDtos
     * @return List<FinalExamDto>
     */
    public List<FinalExamProblemDto> createFinalExamProblems(UUID finalExamId,
        List<FinalExamProblemDto> finalExamProblemDtos) {
        FinalExam saved = finalExamRepository.findById(finalExamId)
            .orElseThrow(RuntimeException::new);

        AtomicInteger index = new AtomicInteger(
            saved.getFinalExamProblems().stream().map(FinalExamProblem::getNumber)
                .reduce(0, Math::max) + 1);

        List<FinalExamProblem> entities = finalExamProblemDtos.stream()
            .map(dto -> convertToEntity(saved, dto, index.getAndIncrement())).toList();
        saved.getFinalExamProblems().addAll(entities);

        return entities.stream().map(this::convertToDto).toList();
    }

    /**
     * FinalExam과 1:N 연관된 FinalExamProblems 조회
     * 기말고사 문제 조회
     * 
     * @param finalExamId
     * @return
     */
    public List<FinalExamProblemDto> findAllFinalExamProblems(UUID finalExamId) {
        return finalExamRepository.findById(finalExamId)
            .orElseThrow(RuntimeException::new).getFinalExamProblems().stream()
            .map(this::convertToDto)
            .toList();
    }


    /**
     * FinalExam entity를 FinalExam dto로 변환
     *
     * @param entity
     * @return FinalExamDto
     */
    private FinalExamDto convertToDto(FinalExam entity) {
        List<UUID> finalExamProblems = entity.getFinalExamProblems().stream()
            .map(FinalExamProblem::getFinalProblemId).toList();

        return new FinalExamDto(entity.getFinalExamId(), entity.getCourse().getCourseId(),
            finalExamProblems, entity.getFinalExamEvaluationStatus(), entity.getTitle(),
            entity.getDescription(), entity.getStartTime(), entity.getEndTime());
    }

    /**
     * FinalExam dto를 FinalExam entity로 변환
     *
     * @param dto
     * @return FinalExam
     */
    private FinalExam convertToEntity(FinalExamDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
            .orElseThrow(RuntimeException::new);

        List<FinalExamProblem> finalExamProblems = dto.getFinalExamProblems().stream()
            .map(finalProblemId ->
                finalExamProblemRepository.findById(finalProblemId)
                    .orElseThrow(RuntimeException::new)).toList();

        return new FinalExam(dto.getFinalExamId(), course, finalExamProblems,
            dto.getEvaluationStatus(), dto.getTitle(), dto.getDescription(), dto.getStartTime(),
            dto.getEndTime());
    }

    private FinalExamProblem convertToEntity(FinalExam finalExam, FinalExamProblemDto dto,
        Integer index) {

        return FinalExamProblem.builder()
            .finalProblemId(dto.getFinalProblemId())
            .finalExam(finalExam)
            .number(index)
            .correctAnswer(dto.getCorrectAnswer())
            .difficulty(dto.getDifficulty())
            .question(dto.getQuestion())
            .choices(dto.getChoices())
            .build();
    }

    private FinalExamProblemDto convertToDto(FinalExamProblem entity) {
        return new FinalExamProblemDto(entity.getFinalProblemId(),
            entity.getFinalExam().getFinalExamId(),
            entity.getNumber(), entity.getCorrectAnswer(), entity.getDifficulty(),
            entity.getQuestion(), entity.getChoices());
    }
}
