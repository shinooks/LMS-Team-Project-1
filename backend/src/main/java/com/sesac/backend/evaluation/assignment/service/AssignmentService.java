package com.sesac.backend.evaluation.assignment.service;

import com.sesac.backend.course.repository.CourseOpeningRepository;
import com.sesac.backend.entity.CourseOpening;
import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.assignment.dto.AssignCreationRequest;
import com.sesac.backend.evaluation.assignment.dto.AssignResponse;
import com.sesac.backend.evaluation.assignment.dto.AssignScoreRequest;
import com.sesac.backend.evaluation.assignment.dto.AssignSubmissionRequest;
import com.sesac.backend.evaluation.assignment.repository.AssignmentRepository;
import com.sesac.backend.evaluation.assignment.repository.StudentRepositoryDemo;
import com.sesac.backend.evaluation.score.domain.Score;
import com.sesac.backend.evaluation.score.repository.ScoreRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin 과제 service Assignment 비즈니스 로직 구현
 */
@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseOpeningRepository courseOpeningRepository;
    private final StudentRepositoryDemo studentRepositoryDemo;
    private final ScoreRepository scoreRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository,
        CourseOpeningRepository courseOpeningRepository,
        StudentRepositoryDemo studentRepositoryDemo, ScoreRepository scoreRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseOpeningRepository = courseOpeningRepository;
        this.studentRepositoryDemo = studentRepositoryDemo;
        this.scoreRepository = scoreRepository;
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
    public AssignSubmissionRequest submitAssignment(AssignSubmissionRequest request) {
        Student student = studentRepositoryDemo.findById(request.getStudentId())
            .orElseThrow(RuntimeException::new);
        CourseOpening courseOpening = courseOpeningRepository.findById(request.getOpeningId())
            .orElseThrow(RuntimeException::new);

        Assignment saved = assignmentRepository.findByStudentAndCourseOpening(student,
            courseOpening);

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(saved.getOpenAt()) || now.isAfter(saved.getDeadline())) {
            throw new RuntimeException("제출기한이 지났습니다.");
        }

        saved.setFile(request.getFile());

        return convertToSubmissionRequest(saved);
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
        CourseOpening courseOpening = courseOpeningRepository.findById(openingId).orElseThrow(RuntimeException::new);
        Student student = studentRepositoryDemo.findById(studentId).orElseThrow(RuntimeException::new);

        Assignment saved = assignmentRepository.findByStudentAndCourseOpening(student, courseOpening);
        return new AssignResponse(saved.getTitle(), saved.getDescription());
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
    private AssignSubmissionRequest convertToSubmissionRequest(Assignment entity) {
        return new AssignSubmissionRequest(entity.getStudent().getStudentId(),
            entity.getCourseOpening().getOpeningId(), entity.getFile());
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
