package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.AssignSubmit;
import com.sesac.backend.assignment.dto.AssignSubmitDto;
import com.sesac.backend.assignment.repository.AssignSubmitRepository;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class AssignSubmitService {

    private final AssignSubmitRepository assignSubmitRepository;

    public AssignSubmitService(AssignSubmitRepository assignSubmitRepository) {
        this.assignSubmitRepository = assignSubmitRepository;
    }

    public AssignSubmitDto findById(UUID assignSubmitId) {
        AssignSubmit entity = assignSubmitRepository.findById(assignSubmitId).orElse(null);
        return entity == null ? null
            : new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
                entity.getStudent(), entity.getAnswer(), entity.getSubmitAt(), entity.getFileName());
    }

    public List<AssignSubmitDto> getAll() {
        return assignSubmitRepository.findAll().stream().map(
            entity -> new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
                entity.getStudent(), entity.getAnswer(), entity.getSubmitAt(), entity.getFileName())).toList();
    }

    public boolean submit(AssignSubmitDto assignSubmitDto) {
        LocalDateTime now = assignSubmitDto.getSubmitAt();
        LocalDateTime deadline = assignSubmitDto.getAssignment().getDeadline();

        boolean flag = false;

        if (now.isBefore(deadline)) {
            assignSubmitRepository.save(
                new AssignSubmit(assignSubmitDto.getAssignSubmitId(),
                    assignSubmitDto.getAssignment(), assignSubmitDto.getStudent(),
                    assignSubmitDto.getAnswer(), now, assignSubmitDto.getFileName()));

            flag = true;
            return flag;
        }

        return flag;
    }

    public AssignSubmitDto update(AssignSubmitDto assignSubmitDto) {
        AssignSubmit entity = assignSubmitRepository.save(
            new AssignSubmit(assignSubmitDto.getAssignSubmitId(), assignSubmitDto.getAssignment(),
                assignSubmitDto.getStudent(), assignSubmitDto.getAnswer(),
                assignSubmitDto.getSubmitAt(), assignSubmitDto.getFileName()));

        return new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
            entity.getStudent(), entity.getAnswer(), entity.getSubmitAt(), entity.getFileName());
    }

    public void delete(UUID assignSubmitId) {
        assignSubmitRepository.deleteById(assignSubmitId);
    }
}
