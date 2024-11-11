package com.sesac.backend.evaluation.assignment.service;

import com.sesac.backend.entity.Student;
import com.sesac.backend.evaluation.assignment.domain.AssignSubmit;
import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.assignment.dto.AssignSubmitDto;
import com.sesac.backend.evaluation.assignment.repository.AssignSubmitRepository;
import com.sesac.backend.evaluation.assignment.repository.AssignmentRepository;
import com.sesac.backend.evaluation.assignment.repository.StudentRepositoryDemo;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dongjin 과제 제출 service AssignSubmit 비즈니스 로직 구현
 */
@Service
public class AssignSubmitService {

    private final AssignSubmitRepository assignSubmitRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepositoryDemo studentRepositoryDemo;

    @Autowired
    public AssignSubmitService(AssignSubmitRepository assignSubmitRepository,
        StudentRepositoryDemo studentRepositoryDemo, AssignmentRepository assignmentRepository) {
        this.assignSubmitRepository = assignSubmitRepository;
        this.studentRepositoryDemo = studentRepositoryDemo;
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * AssignSubmit 테이블 레코드 assignSubmitId(PK)로 조회
     *
     * @param assignSubmitId
     * @return AssignSubmitDto
     */
    public AssignSubmitDto findById(UUID assignSubmitId) {
        return assignSubmitRepository.findById(assignSubmitId).stream().map(this::convertToDto)
            .findFirst().orElseThrow(RuntimeException::new);
    }

    /**
     * AssignSubmit 테이블 레코드 전체 조회
     *
     * @return List<AssignSubmitDto>
     */
    public List<AssignSubmitDto> getAll() {
        return assignSubmitRepository.findAll().stream().map(this::convertToDto).toList();
    }

    /**
     * AssignSubmit 테이블 레코드 생성 (과제 제출) AssignSubmit 테이블의 제출일시, Assignment 테이블의 제출기한을 비교해서 제출일시가 제출기한
     * 이전이면 제출 성공, 제출일시가 제출기한 이후면 제출 실패
     *
     * @param assignSubmitDto
     * @return 성공 true, 실패 false
     */
    public AssignSubmitDto submit(AssignSubmitDto assignSubmitDto) {
        LocalDateTime regDate = LocalDateTime.now();

        LocalDateTime deadline = assignSubmitRepository.findById(assignSubmitDto.getAssignId())
            .orElseThrow(RuntimeException::new).getAssignment().getDeadline();

        if (!regDate.isBefore(deadline)) {
            throw new RuntimeException();
        }

        return convertToDto(assignSubmitRepository.save(convertToEntity(assignSubmitDto)));
    }

    /**
     * AssignSubmit 테이블 레코드 수정
     *
     * @param assignSubmitDto
     * @return AssignSubmitDto
     */
    public AssignSubmitDto update(AssignSubmitDto assignSubmitDto) {
        AssignSubmit saved = assignSubmitRepository.findById(assignSubmitDto.getAssignId())
            .orElseThrow(RuntimeException::new);

        saved.setAnswer(assignSubmitDto.getAnswer());
        saved.setFileName(assignSubmitDto.getFileName());

        return convertToDto(assignSubmitRepository.save(saved));
    }

    /**
     * AssignSubmit 테이블 레코드 assignSubmitId(PK)로 삭제
     *
     * @param assignSubmitId
     */
    public void delete(UUID assignSubmitId) {
        assignSubmitRepository.findById(assignSubmitId).orElseThrow(RuntimeException::new);
        assignSubmitRepository.deleteById(assignSubmitId);
    }

    private AssignSubmitDto convertToDto(AssignSubmit entity) {
        return AssignSubmitDto.builder()
            .assignSubmitId(entity.getAssignSubmitId())
            .assignId(entity.getAssignment().getAssignId())
            .studentId(entity.getStudent().getStudentId())
            .answer(entity.getAnswer())
            .fileName(entity.getFileName())
            .build();
    }

    private AssignSubmit convertToEntity(AssignSubmitDto dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignId())
            .orElseThrow(RuntimeException::new);
        Student student = studentRepositoryDemo.findById(dto.getStudentId())
            .orElseThrow(RuntimeException::new);

        return AssignSubmit.builder().assignSubmitId(dto.getAssignSubmitId())
            .assignment(assignment).student(student).answer(dto.getAnswer()).fileName(dto.getFileName()).build();
    }
}
