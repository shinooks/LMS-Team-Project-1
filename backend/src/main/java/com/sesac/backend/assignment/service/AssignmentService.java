package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.Assignment;
import com.sesac.backend.assignment.dto.AssignmentDto;
import com.sesac.backend.assignment.repository.AssignmentRepository;
import java.util.List;
import java.util.UUID;

import com.sesac.backend.course.repository.CourseRepository;
import com.sesac.backend.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
    }

    public AssignmentDto save(AssignmentDto assignmentDto) {
        Course courseEntity = courseRepository.findById(assignmentDto.getAssignId()).orElse(null);

        Assignment assignment = assignmentRepository.save(
            new Assignment(assignmentDto.getAssignId(), courseEntity,
                assignmentDto.getTitle(), assignmentDto.getDescription(),
                assignmentDto.getDeadline()));

        AssignmentDto dto = new AssignmentDto(assignment.getAssignId(), assignment.getCourse().getCourseId(),
            assignment.getTitle(), assignment.getDescription(), assignment.getDeadline());

        return dto;
    }

    public List<AssignmentDto> getAll() {
        return assignmentRepository.findAll().stream()
            .map(entity -> new AssignmentDto(entity.getAssignId(), entity.getCourse().getCourseId(),
                entity.getTitle(), entity.getDescription(),
                entity.getDeadline())).toList();
    }

    public AssignmentDto findById(UUID id) {
        Assignment entity = assignmentRepository.findById(id).orElse(null);

        return entity == null ? null
            : new AssignmentDto(entity.getAssignId(), entity.getCourse().getCourseId(), entity.getTitle(),
                entity.getDescription(), entity.getDeadline());
    }

    public void delete(UUID id) {
        assignmentRepository.deleteById(id);
    }
}
