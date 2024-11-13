package com.sesac.backend.evaluation.exam.service;

import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.enrollment.domain.classEnrollment.CourseEnrollment;
import com.sesac.backend.enrollment.repository.ClassesEnrollmentRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.enums.Correctness;
import com.sesac.backend.evaluation.enums.Type;
import com.sesac.backend.evaluation.exam.domain.Exam;
import com.sesac.backend.evaluation.exam.domain.ExamProblem;
import com.sesac.backend.evaluation.exam.dto.request.ExamAnswerDto;
import com.sesac.backend.evaluation.exam.dto.request.ExamCreationRequest;
import com.sesac.backend.evaluation.exam.dto.request.ExamProblemCreationDto;
import com.sesac.backend.evaluation.exam.dto.request.ExamSubmissionRequest;
import com.sesac.backend.evaluation.exam.dto.request.ExamRequest;
import com.sesac.backend.evaluation.exam.dto.response.ExamCreationResponse;
import com.sesac.backend.evaluation.exam.dto.response.ExamProblemReadDto;
import com.sesac.backend.evaluation.exam.dto.response.ExamReadResponse;
import com.sesac.backend.evaluation.exam.dto.response.ExamResponse;
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
 * @author dongjin 시험 service
 * <p>
 * Exam 비즈니스 로직 구현
 */
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamProblemRepository examProblemRepository;
    private final CourseOpeningRepository courseOpeningRepository;
    private final ScoreRepository scoreRepository;
    private final ClassesEnrollmentRepository classesEnrollmentRepository;

    @Autowired
    public ExamService(ExamRepository examRepository,
        ExamProblemRepository examProblemRepository,
        CourseOpeningRepository courseOpeningRepository,
        ScoreRepository scoreRepository,
        ClassesEnrollmentRepository classesEnrollmentRepository) {
        this.examRepository = examRepository;
        this.examProblemRepository = examProblemRepository;
        this.courseOpeningRepository = courseOpeningRepository;
        this.scoreRepository = scoreRepository;
        this.classesEnrollmentRepository = classesEnrollmentRepository;
    }

    /**
     * Exam 생성
     *
     * @param request
     * @return FinalExamDto
     */
    public ExamCreationResponse createExam(ExamCreationRequest request) {
        classesEnrollmentRepository
            .findAllByCourseOpeningOpeningId(request.getOpeningId()).stream()
            .map(CourseEnrollment::getStudent)
            .forEach(student -> examRepository.save(convertToEntity(request, student)));

        return new ExamCreationResponse(request.getOpeningId(), request.getType(),
            request.getStartTime(), request.getEndTime());
    }

    /**
     * 시험 목록 조회
     *
     * @param request 시험 목록 요청
     * @return 시험 목록
     */
    public ExamResponse getMyExam(ExamRequest request) {
        Exam exam = examRepository.findByCourseOpeningOpeningIdAndStudentStudentIdAndType(
                request.getOpeningId(), request.getStudentId(), request.getType())
            .orElseThrow(RuntimeException::new);

        CourseOpening courseOpening = exam.getCourseOpening();

        StringBuffer title = new StringBuffer(courseOpening.getYear()).append("학년도 ")
            .append(courseOpening.getSemester()).append("학기 ").append(exam.getType().getValue());

        return new ExamResponse(exam.getExamId(), title.toString());
    }

    /**
     * 시험 문제 조회
     *
     * @param finalExamId
     * @return FinalExamDto
     */
    public ExamReadResponse getMyExamProblems(UUID finalExamId) {
        return examRepository.findById(finalExamId).stream()
            .map(this::convertToReadResponse).findFirst()
            .orElseThrow(RuntimeException::new);
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

    private ExamReadResponse convertToReadResponse(Exam entity) {
        List<ExamProblemReadDto> problemReadDtos = entity.getExamProblems().stream()
            .map(this::convertToProblemReadDto).toList();

        return new ExamReadResponse(entity.getExamId(), problemReadDtos, entity.getStartTime(),
            entity.getEndTime());
    }

    private ExamProblemReadDto convertToProblemReadDto(ExamProblem problem) {
        return new ExamProblemReadDto(problem.getProblemId(), problem.getNumber(),
            problem.getDifficulty(), problem.getQuestion(), problem.getChoices());
    }

    private Exam convertToEntity(ExamCreationRequest request, Student student) {
        CourseOpening courseOpening = courseOpeningRepository.findById(request.getOpeningId())
            .orElseThrow(RuntimeException::new);

        List<ExamProblem> examProblems = request.getProblems().stream()
            .map(this::convertToProblem).toList();

        return Exam.builder().courseOpening(courseOpening)
            .student(student)
            .examProblems(examProblems).type(request.getType()).startTime(request.getStartTime())
            .endTime(request.getEndTime()).build();
    }

    private ExamProblem convertToProblem(ExamProblemCreationDto dto) {
        return ExamProblem.builder().number(dto.getNumber()).correctAnswer(dto.getCorrectAnswer())
            .difficulty(dto.getDifficulty()).question(
                dto.getQuestion()).choices(dto.getChoices()).build();
    }
}
