package com.sesac.backend.evaluation.assignment.service;

import com.sesac.backend.aws.dto.FileUploadResponse;
import com.sesac.backend.aws.service.S3Service;
import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.assignment.dto.request.AssignCreationRequest;
import com.sesac.backend.evaluation.assignment.dto.request.AssignScoreRequest;
import com.sesac.backend.evaluation.assignment.dto.request.AssignSubmissionRequest;
import com.sesac.backend.evaluation.assignment.dto.response.AssignResponse;
import com.sesac.backend.evaluation.assignment.dto.response.AssignSubmissionResponse;
import com.sesac.backend.evaluation.assignment.repository.AssignmentRepository;
import com.sesac.backend.evaluation.assignment.repository.StudentRepositoryDemo;
import com.sesac.backend.evaluation.copyleaks.service.CopyleaksService;
import com.sesac.backend.evaluation.enums.Priority;
import com.sesac.backend.evaluation.score.domain.Score;
import com.sesac.backend.evaluation.score.repository.ScoreRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin 과제 service Assignment 비즈니스 로직 구현
 */
@Slf4j
@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseOpeningRepository courseOpeningRepository;
    private final StudentRepositoryDemo studentRepositoryDemo;
    private final ScoreRepository scoreRepository;
    private final CopyleaksService copyleaksService;
    private final S3Service s3Service;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository,
        CourseOpeningRepository courseOpeningRepository,
        StudentRepositoryDemo studentRepositoryDemo, ScoreRepository scoreRepository,
        CopyleaksService copyleaksService, S3Service s3Service) {
        this.assignmentRepository = assignmentRepository;
        this.courseOpeningRepository = courseOpeningRepository;
        this.studentRepositoryDemo = studentRepositoryDemo;
        this.scoreRepository = scoreRepository;
        this.copyleaksService = copyleaksService;
        this.s3Service = s3Service;
    }

    public List<AssignResponse> getAllAssignments(UUID studentId) {
        LocalDate now = LocalDate.now();

        List<Assignment> assignments = assignmentRepository.findAllByStudentStudentId(studentId);

        assignments.forEach(assignment -> {
            Period period = Period.between(assignment.getDeadline(), now);
            assignment.setPriority(period.getDays() < 1 ? Priority.HIGH
                : period.getDays() < 5 ? Priority.MEDIUM : Priority.LOW);
            assignmentRepository.save(assignment);
        });

        return assignmentRepository.findAllByStudentStudentId(studentId).stream()
            .map(this::convertToAssignResponse).toList();
    }

    /**
     * 과제 생성
     *
     * @param request
     * @return
     */
    public AssignCreationRequest createAssignment(AssignCreationRequest request) {
        return convertToCreationRequest(assignmentRepository.save(convertToEntity(request)));
    }

    /**
     * 과제 제출
     *
     * @param request
     * @return
     */
    public AssignSubmissionResponse submitAssignment(AssignSubmissionRequest request) {

        // 제출할 과제 찾기
        Student student = studentRepositoryDemo.findById(request.getStudentId())
            .orElseThrow(RuntimeException::new);
        CourseOpening courseOpening = courseOpeningRepository.findById(request.getOpeningId())
            .orElseThrow(RuntimeException::new);

        Assignment entity = assignmentRepository.findByStudentAndCourseOpening(student,
            courseOpening);

        // 제출기한 유효성 검증
        LocalDate now = LocalDate.now();
        if (now.isBefore(entity.getOpenAt()) || now.isAfter(entity.getDeadline())) {
            throw new RuntimeException("제출기한이 지났습니다.");
        }

        // 표절검사
        try {
            copyleaksService.checkPlagiarism(entity.getAssignId());
        } catch (IOException e) {
            log.error("Failed to Copyleaks");
        }

        // 파일 저장
//        FileUploadResponse response = s3Service.uploadFile(request.getFile());
//        entity.setSavedFileName(response.getSavedFileName());
        Assignment saved = assignmentRepository.save(entity);

        return new AssignSubmissionResponse(saved.getAssignId(), saved.getStudent().getStudentId(),
            saved.getCourseOpening().getOpeningId());
    }

    /**
     * 과제 채점
     *
     * @param request
     * @return
     */
    public AssignScoreRequest updateAssignScore(AssignScoreRequest request) {
        Student student = studentRepositoryDemo.findById(request.getStudentId())
            .orElseThrow(RuntimeException::new);
        CourseOpening courseOpening = courseOpeningRepository.findById(request.getOpeningId())
            .orElseThrow(RuntimeException::new);

        Assignment saved = assignmentRepository.findByStudentAndCourseOpening(student,
            courseOpening);

        Score score = scoreRepository.findByStudentAndCourseOpening(student, courseOpening).orElse(
            Score.builder().student(student).build());

        score.setAssignment(saved);
        score.setAssignScore(request.getScore());

        return request;
    }

    /**
     * 과제 조회
     *
     * @param openingId
     * @param studentId
     * @return
     */
    public AssignResponse findAssign(UUID openingId, UUID studentId) {
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId)
            .orElseThrow(RuntimeException::new);
        Student student = studentRepositoryDemo.findById(studentId)
            .orElseThrow(RuntimeException::new);

        Assignment saved = assignmentRepository.findByStudentAndCourseOpening(student,
            courseOpening);

        return new AssignResponse(saved.getAssignId(), saved.getTitle(), saved.getTitle(),
            saved.getDeadline(), saved.getStatus(), saved.getPriority());
    }

    private AssignResponse convertToAssignResponse(Assignment entity) {
        return new AssignResponse(entity.getAssignId(), entity.getTitle(),
            entity.getCourseOpening().getCourse().getCourseName(), entity.getDeadline(),
            entity.getStatus(), entity.getPriority());
    }

    /**
     * AssignCreationRequest > Assignment
     *
     * @param dto
     * @return
     */
    private Assignment convertToEntity(AssignCreationRequest dto) {
        CourseOpening courseOpening = courseOpeningRepository.findById(dto.getOpeningId())
            .orElseThrow(RuntimeException::new);

        Student student = studentRepositoryDemo.findById(dto.getStudentId())
            .orElseThrow(RuntimeException::new);

        return Assignment.builder().courseOpening(courseOpening).student(student)
            .title(dto.getTitle()).description(dto.getDescription()).openAt(dto.getOpenAt())
            .deadline(dto.getDeadline()).build();
    }

    /**
     * Assignment > AssignSubmissionRequest
     *
     * @param entity
     * @return
     */
    private AssignSubmissionResponse convertToSubmissionRequest(Assignment entity) {
        return new AssignSubmissionResponse(entity.getAssignId(),
            entity.getStudent().getStudentId(),
            entity.getCourseOpening().getOpeningId());
    }

    /**
     * Assignment > AssignCreationRequest
     *
     * @param entity
     * @return
     */
    private AssignCreationRequest convertToCreationRequest(Assignment entity) {
        return new AssignCreationRequest(entity.getAssignId(), entity.getStudent().getStudentId(),
            entity.getTitle(), entity.getDescription(), entity.getOpenAt(), entity.getDeadline());
    }
}
