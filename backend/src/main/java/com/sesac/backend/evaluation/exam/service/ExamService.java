package com.sesac.backend.evaluation.exam.service;

import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.repository.StudentRepositoryDemo;
import com.sesac.backend.evaluation.enums.Correctness;
import com.sesac.backend.evaluation.enums.Type;
import com.sesac.backend.evaluation.exam.domain.Exam;
import com.sesac.backend.evaluation.exam.domain.ExamProblem;
import com.sesac.backend.evaluation.exam.dto.ExamAnswerDto;
import com.sesac.backend.evaluation.exam.dto.ExamCreationRequest;
import com.sesac.backend.evaluation.exam.dto.ExamProblemDto;
import com.sesac.backend.evaluation.exam.dto.ExamSubmissionRequest;
import com.sesac.backend.evaluation.exam.repository.ExamProblemRepository;
import com.sesac.backend.evaluation.exam.repository.ExamRepository;
import com.sesac.backend.evaluation.score.domain.Score;
import com.sesac.backend.evaluation.score.repository.ScoreRepository;
import java.time.LocalDateTime;
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
    private final CourseOpeningRepository courseOpeningRepository;
    private final ScoreRepository scoreRepository;
    private final StudentRepositoryDemo studentRepositoryDemo;

    @Autowired
    public ExamService(ExamRepository examRepository,
        ExamProblemRepository examProblemRepository,
        CourseOpeningRepository courseOpeningRepository, ScoreRepository scoreRepository,
        StudentRepositoryDemo studentRepositoryDemo) {
        this.examRepository = examRepository;
        this.examProblemRepository = examProblemRepository;
        this.courseOpeningRepository = courseOpeningRepository;
        this.scoreRepository = scoreRepository;
        this.studentRepositoryDemo = studentRepositoryDemo;
    }

    /**
     * Exam 테이블 레코드 finalExamId(PK)로 조회
     *
     * @param finalExamId
     * @return FinalExamDto
     */
    public ExamCreationRequest getByExamId(UUID finalExamId) {
        return examRepository.findById(finalExamId).stream()
            .map(this::convertToDto).findFirst()
            .orElseThrow(RuntimeException::new);
    }

    /**
     * Exam 테이블 레코드 전체조회
     *
     * @return List<FinalExamDto>
     */
    public List<ExamCreationRequest> getAllExams() {
        return examRepository.findAll().stream().map(this::convertToDto).toList();
    }

    /**
     * Exam 테이블 레코드 생성
     *
     * @param examCreationRequest
     * @return FinalExamDto
     */
    public ExamCreationRequest createExam(ExamCreationRequest examCreationRequest) {
        return convertToDto(examRepository.save(convertToEntity(examCreationRequest)));
    }

    /**
     * Exam 테이블 레코드 업데이트
     *
     * @param examCreationRequest
     * @return FinalExamDto
     */
    public ExamCreationRequest updateExam(ExamCreationRequest examCreationRequest) {
        Exam saved = examRepository.findById(examCreationRequest.getExamId())
            .orElseThrow(RuntimeException::new);

        saved.setStartTime(examCreationRequest.getStartTime());
        saved.setEndTime(examCreationRequest.getEndTime());
        return convertToDto(examRepository.save(saved));
    }

    /**
     * Exam 테이블 레코드 finalExamId(PK)로 삭제
     *
     * @param finalExamId
     */
    public void deleteExam(UUID finalExamId) {
        examRepository.findById(finalExamId).orElseThrow(RuntimeException::new);
        examRepository.deleteById(finalExamId);
    }

    /**
     * Exam과 1:N 연관된 FinalExamProblems 업데이트 시험 문제 출제
     *
     * @param request
     * @return ExamCreationRequest
     */
    public ExamCreationRequest saveExamProblems(ExamCreationRequest request) {
        Exam entity = convertToEntity(request);

        Exam saved = examRepository.save(entity);

        return convertToDto(saved);
    }

    /**
     * Exam과 1:N 연관된 FinalExamProblems 조회 시험 문제 조회
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
     * 시험 응시(제출)
     *
     * @param request
     * @return
     */
    public ExamSubmissionRequest submit(ExamSubmissionRequest request) {
        Exam exam = examRepository.findById(request.getExamId()).orElseThrow(RuntimeException::new);
        Student student = exam.getStudent();
        CourseOpening courseOpening = exam.getCourseOpening();

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(exam.getStartTime()) || now.isAfter(exam.getEndTime())) {
            throw new RuntimeException("제출기한이 지났습니다.");
        }

        int point = 0;

        for (ExamAnswerDto examAnswerDto : request.getAnswers()) {
            ExamProblem problem = exam.getExamProblems().stream()
                .filter(p -> p.getProblemId().equals(examAnswerDto.getProblemId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ExamProblem not found"));

            if (problem.getCorrectAnswer() == examAnswerDto.getSelectedAnswer()) {
                problem.setCorrectness(Correctness.CORRECT);
                point += problem.getDifficulty().getPoint();
            } else {
                problem.setCorrectness(Correctness.WRONG);
            }
            problem.setSelectedAnswer(examAnswerDto.getSelectedAnswer()); // 선택된 답안 설정
            examProblemRepository.save(problem); // 문제 저장
        }

        // `Score`가 기존에 존재하는지 확인 후 생성 또는 업데이트
        Score score = scoreRepository.findByStudentAndCourseOpening(student, courseOpening)
            .orElseGet(() -> Score.builder().student(student).build());

        if (exam.getType() == Type.MIDTERM) {
            score.setMidtermExam(exam);
            score.setMidtermExamScore(point);
        } else {
            score.setFinalExam(exam);
            score.setFinalExamScore(point);
        }

        scoreRepository.save(score);
        return ExamSubmissionRequest.builder().examId(exam.getExamId())
            .studentId(exam.getStudent().getStudentId()).answers(exam.getExamProblems().stream()
                .map(entity -> ExamAnswerDto.builder().problemId(entity.getProblemId())
                    .number(entity.getNumber()).selectedAnswer(entity.getSelectedAnswer()).build())
                .toList()).build();
    }


    /**
     * Exam entity를 ExamCreationRequest로 변환
     *
     * @param entity
     * @return FinalExamDto
     */
    private ExamCreationRequest convertToDto(Exam entity) {
        List<ExamProblemDto> examProblems = entity.getExamProblems().stream()
            .map(this::convertToDto).toList();

        return new ExamCreationRequest(entity.getExamId(), entity.getCourseOpening().getOpeningId(),
            entity.getStudent().getStudentId(), examProblems, entity.getType(),
            entity.getStartTime(), entity.getEndTime());
    }

    /**
     * FinalExam dto를 FinalExam entity로 변환
     *
     * @param request
     * @return FinalExam
     */
    private Exam convertToEntity(ExamCreationRequest request) {
        CourseOpening courseOpening = courseOpeningRepository.findById(request.getOpeningId())
            .orElseThrow(RuntimeException::new);

        List<ExamProblem> examProblems = request.getProblems().stream()
            .map(problemDto ->
                examProblemRepository.findById(problemDto.getProblemId())
                    .orElseThrow(RuntimeException::new)).toList();

        Student student = studentRepositoryDemo.findById(request.getStudentId())
            .orElseThrow(RuntimeException::new);

        return Exam.builder().examId(request.getExamId()).courseOpening(courseOpening)
            .student(student)
            .examProblems(examProblems).type(request.getType()).startTime(request.getStartTime())
            .endTime(request.getEndTime()).build();
    }

    private ExamProblemDto convertToDto(ExamProblem entity) {
        return new ExamProblemDto(entity.getProblemId(),
            entity.getNumber(), entity.getCorrectAnswer(),
            entity.getDifficulty(), entity.getQuestion(),
            entity.getChoices());
    }
}
