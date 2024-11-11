package com.sesac.backend.evaluation.exam.service;

import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.repository.StudentRepositoryDemo;
import com.sesac.backend.evaluation.exam.domain.Exam;
import com.sesac.backend.evaluation.exam.domain.ExamAnswer;
import com.sesac.backend.evaluation.exam.domain.ExamSubmit;
import com.sesac.backend.evaluation.exam.dto.ExamAnswerDto;
import com.sesac.backend.evaluation.exam.dto.ExamSubmitDto;
import com.sesac.backend.evaluation.exam.repository.ExamAnswerRepository;
import com.sesac.backend.evaluation.exam.repository.ExamRepository;
import com.sesac.backend.evaluation.exam.repository.ExamSubmitRepository;
import com.sesac.backend.evaluation.score.dto.ScoreDto;
import com.sesac.backend.evaluation.score.repository.ScoreRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamSubmitService {

    private final ExamSubmitRepository examSubmitRepository;
    private final ExamAnswerRepository examAnswerRepository;
    private final ExamRepository examRepository;
    private final ScoreRepository scoreRepository;
    private final StudentRepositoryDemo studentRepositoryDemo;

    @Autowired
    public ExamSubmitService(ExamSubmitRepository examSubmitRepository,
        ExamAnswerRepository examAnswerRepository, ExamRepository examRepository,
        ScoreRepository scoreRepository, StudentRepositoryDemo studentRepositoryDemo) {
        this.examSubmitRepository = examSubmitRepository;
        this.examAnswerRepository = examAnswerRepository;
        this.examRepository = examRepository;
        this.scoreRepository = scoreRepository;
        this.studentRepositoryDemo = studentRepositoryDemo;
    }

    public ExamSubmitDto getByExamSubmitId(UUID submitId) {
        return examSubmitRepository.findById(submitId).stream()
            .map(this::convertToDto).findFirst().orElseThrow(RuntimeException::new);
    }

    public List<ExamSubmitDto> getAllExamSubmits() {
        return examSubmitRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public ExamSubmitDto createExamSubmit(ExamSubmit examSubmit) {
        return convertToDto(examSubmitRepository.save(examSubmit));
    }

    public ExamSubmitDto updateExamSubmit(ExamSubmitDto examSubmitDto) {
        ExamSubmit saved = examSubmitRepository.findById(examSubmitDto.getStudentId())
            .orElseThrow(RuntimeException::new);

        saved.setSubmissionStatus(examSubmitDto.getSubmissionStatus());

        return convertToDto(examSubmitRepository.save(saved));
    }

    public void deleteExamSubmit(UUID examSubmitId) {
        examSubmitRepository.findById(examSubmitId).orElseThrow(RuntimeException::new);
        examSubmitRepository.deleteById(examSubmitId);
    }

    public List<ExamAnswerDto> createExamAnswers(UUID submitId,
        List<ExamAnswerDto> examAnswerDtos) {
        ExamSubmit saved = examSubmitRepository.findById(submitId)
            .orElseThrow(RuntimeException::new);

        saved.getExamAnswers().clear();

        List<ExamAnswer> entities = examAnswerDtos.stream()
            .map(dto -> convertToEntity(saved, dto)).toList();

        saved.getExamAnswers().addAll(entities);

        return entities.stream().map(this::convertToDto).toList();
    }

    public List<ExamAnswerDto> findAllExamAnswers(UUID submitId) {
        return examSubmitRepository.findById(submitId)
            .orElseThrow(RuntimeException::new).getExamAnswers().stream()
            .map(this::convertToDto).toList();
    }
    

    private ExamSubmitDto convertToDto(ExamSubmit entity) {
        List<UUID> examAnswers = entity.getExamAnswers().stream()
            .map(ExamAnswer::getAnswerId).toList();

        return new ExamSubmitDto(entity.getSubmitId(), entity.getExam().getExamId(),
            entity.getStudent()
                .getStudentId(), examAnswers, entity.getSubmissionStatus());
    }

    private ExamSubmit convertToEntity(ExamSubmitDto dto) {
        Exam exam = examRepository.findById(dto.getExamId()).orElseThrow(RuntimeException::new);
        Student student = studentRepositoryDemo.findById(dto.getStudentId())
            .orElseThrow(RuntimeException::new);
        List<ExamAnswer> examAnswers = dto.getExamAnswers().stream().map(
                answerId -> examAnswerRepository.findById(answerId).orElseThrow(RuntimeException::new))
            .toList();

        return new ExamSubmit(dto.getSubmitId(), exam, student, examAnswers,
            dto.getSubmissionStatus());
    }

    private ExamAnswer convertToEntity(ExamSubmit submit, ExamAnswerDto dto) {

        return ExamAnswer.builder()
            .answerId(dto.getAnswerId())
            .examSubmit(submit)
            .number(dto.getNumber())
            .selected(dto.getSelected())
            .correctness(dto.getCorrectness())
            .build();
    }

    private ExamAnswerDto convertToDto(ExamAnswer entity) {
        return new ExamAnswerDto(entity.getAnswerId(), entity.getExamSubmit().getSubmitId(),
            entity.getNumber(), entity.getSelected(), entity.getCorrectness());
    }
}