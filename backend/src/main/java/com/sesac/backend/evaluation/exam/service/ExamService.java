package com.sesac.backend.evaluation.exam.service;

import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
import com.sesac.backend.evaluation.exam.domain.Exam;
import com.sesac.backend.evaluation.exam.domain.ExamProblem;
import com.sesac.backend.evaluation.exam.dto.ExamDto;
import com.sesac.backend.evaluation.exam.dto.ExamProblemDto;
import com.sesac.backend.evaluation.exam.repository.ExamProblemRepository;
import com.sesac.backend.evaluation.exam.repository.ExamRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin 기말고사 service FinalExam 비즈니스 로직 구현
 */
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamProblemRepository examProblemRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ExamService(ExamRepository examRepository,
        ExamProblemRepository examProblemRepository, CourseRepository courseRepository) {
        this.examRepository = examRepository;
        this.examProblemRepository = examProblemRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * FinalExam 테이블 레코드 finalExamId(PK)로 조회
     *
     * @param finalExamId
     * @return FinalExamDto
     */
    public ExamDto getByExamId(UUID finalExamId) {
        return examRepository.findById(finalExamId).stream()
            .map(this::convertToDto).findFirst()
            .orElseThrow(RuntimeException::new);
    }

    /**
     * FinalExam 테이블 레코드 전체조회
     *
     * @return List<FinalExamDto>
     */
    public List<ExamDto> getAllExams() {
        return examRepository.findAll().stream().map(this::convertToDto).toList();
    }

    /**
     * FinalExam 테이블 레코드 생성
     *
     * @param examDto
     * @return FinalExamDto
     */
    public ExamDto createExam(ExamDto examDto) {
        return convertToDto(examRepository.save(convertToEntity(examDto)));
    }

    /**
     * FinalExam 테이블 레코드 업데이트
     *
     * @param examDto
     * @return FinalExamDto
     */
    public ExamDto updateExam(ExamDto examDto) {
        Exam saved = examRepository.findById(examDto.getExamId())
            .orElseThrow(RuntimeException::new);

        saved.setStartTime(examDto.getStartTime());
        saved.setEndTime(examDto.getEndTime());
        return convertToDto(examRepository.save(saved));
    }

    /**
     * FinalExam 테이블 레코드 finalExamId(PK)로 삭제
     *
     * @param finalExamId
     */
    public void deleteExam(UUID finalExamId) {
        examRepository.findById(finalExamId).orElseThrow(RuntimeException::new);
        examRepository.deleteById(finalExamId);
    }

    /**
     * FinalExam과 1:N 연관된 FinalExamProblems 업데이트 시험 문제 출제
     *
     * @param examId
     * @param examProblemDtos
     * @return List<FinalExamDto>
     */
    public List<ExamProblemDto> createExamProblems(UUID examId,
        List<ExamProblemDto> examProblemDtos) {
        Exam saved = examRepository.findById(examId)
            .orElseThrow(RuntimeException::new);

        saved.getExamProblems().clear();

        List<ExamProblem> entities = examProblemDtos.stream()
            .map(dto -> convertToEntity(saved, dto)).toList();

        saved.getExamProblems().addAll(entities);

        return entities.stream().map(this::convertToDto).toList();
    }

    /**
     * FinalExam과 1:N 연관된 FinalExamProblems 조회 시험 문제 조회
     *
     * @param finalExamId
     * @return
     */
    public List<ExamProblemDto> findAllExamProblems(UUID finalExamId) {
        return examRepository.findById(finalExamId)
            .orElseThrow(RuntimeException::new).getExamProblems().stream()
            .map(this::convertToDto)
            .toList();
    }


    /**
     * FinalExam entity를 FinalExam dto로 변환
     *
     * @param entity
     * @return FinalExamDto
     */
    private ExamDto convertToDto(Exam entity) {
        List<UUID> examProblems = entity.getExamProblems().stream()
            .map(ExamProblem::getProblemId).toList();

        return new ExamDto(entity.getExamId(), entity.getCourse().getCourseId(),
            examProblems, entity.getType(), entity.getFinalExamEvaluationStatus(),
            entity.getTitle(),
            entity.getDescription(), entity.getStartTime(), entity.getEndTime());
    }

    /**
     * FinalExam dto를 FinalExam entity로 변환
     *
     * @param dto
     * @return FinalExam
     */
    private Exam convertToEntity(ExamDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
            .orElseThrow(RuntimeException::new);

        List<ExamProblem> examProblems = dto.getExamProblems().stream()
            .map(problemId ->
                examProblemRepository.findById(problemId)
                    .orElseThrow(RuntimeException::new)).toList();

        return new Exam(dto.getExamId(), course, examProblems, dto.getType(),
            dto.getEvaluationStatus(), dto.getTitle(), dto.getDescription(), dto.getStartTime(),
            dto.getEndTime());
    }

    private ExamProblem convertToEntity(Exam exam, ExamProblemDto dto) {

        return ExamProblem.builder()
            .problemId(dto.getProblemId())
            .exam(exam)
            .number(dto.getNumber())
            .correctAnswer(dto.getCorrectAnswer())
            .difficulty(dto.getDifficulty())
            .question(dto.getQuestion())
            .choices(dto.getChoices())
            .build();
    }

    private ExamProblemDto convertToDto(ExamProblem entity) {
        return new ExamProblemDto(entity.getProblemId(),
            entity.getExam().getExamId(),
            entity.getNumber(), entity.getCorrectAnswer(), entity.getDifficulty(),
            entity.getQuestion(), entity.getChoices());
    }
}
