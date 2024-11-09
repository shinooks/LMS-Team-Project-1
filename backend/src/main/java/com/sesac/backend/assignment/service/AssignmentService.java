package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.Assignment;
import com.sesac.backend.assignment.dto.AssignmentDto;
import com.sesac.backend.assignment.repository.AssignmentRepository;
import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin 과제 service Assignment 비즈니스 로직 구현
 */
@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository,
        CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Assignment 테이블 레코드 생성
     *
     * @param assignmentDto
     * @return AssignmentDto
     */
    public AssignmentDto createAssign(AssignmentDto assignmentDto) {
        return convertToDto(assignmentRepository.save(convertToEntity(assignmentDto)));
    }

    /**
     * Assignment 테이블 레코드 수정
     *
     * @param assignmentDto
     * @return AssignmentDto
     */
    public AssignmentDto updateAssign(AssignmentDto assignmentDto) {
        Assignment saved = assignmentRepository.findById(assignmentDto.getAssignId())
            .orElseThrow(RuntimeException::new);

        Course course = courseRepository.findById(assignmentDto.getCourseId())
            .orElseThrow(RuntimeException::new);
        saved.setAssign(course, assignmentDto.getTitle(), assignmentDto.getDescription(),
            assignmentDto.getDeadline());

        return convertToDto(assignmentRepository.save(saved));
    }

    /**
     * Assignment 테이블 전체조회
     *
     * @return List<AssignmentDto>
     */
    public List<AssignmentDto> findAll() {
        return assignmentRepository.findAll().stream()
            .map(entity -> new AssignmentDto(entity.getAssignId(),
                entity.getCourse().getCourseId(), entity.getTitle(), entity.getDescription(),
                entity.getDeadline())).toList();
    }

    /**
     * Assignment 테이블 레코드 assignId(PK)로 조회
     *
     * @param assignId
     * @return AssignmentDto
     */
    public AssignmentDto findById(UUID assignId) {
        return convertToDto(
            assignmentRepository.findById(assignId).orElseThrow(RuntimeException::new));
    }

    /**
     * Assignment 테이블 레코드 assignId(PK)로 삭제
     *
     * @param assignId
     */
    public void delete(UUID assignId) {
        assignmentRepository.findById(assignId).orElseThrow(RuntimeException::new);
        assignmentRepository.deleteById(assignId);
    }

    private AssignmentDto convertToDto(Assignment entity) {
        return new AssignmentDto(entity.getAssignId(), entity.getCourse().getCourseId(),
            entity.getTitle(),
            entity.getDescription(), entity.getDeadline());
    }

    private Assignment convertToEntity(AssignmentDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
            .orElseThrow(RuntimeException::new);

        return new Assignment(dto.getAssignId(), course, dto.getTitle(), dto.getDescription(),
            dto.getDeadline());
    }
}
