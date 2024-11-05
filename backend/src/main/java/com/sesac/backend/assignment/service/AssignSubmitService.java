package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.AssignSubmit;
import com.sesac.backend.assignment.dto.AssignSubmitDto;
import com.sesac.backend.assignment.repository.AssignSubmitDao;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class AssignSubmitService {

    private final AssignSubmitDao assignSubmitDao;

    public AssignSubmitService(AssignSubmitDao assignSubmitDao) {
        this.assignSubmitDao = assignSubmitDao;
    }

    public AssignSubmitDto findById(UUID assignSubmitId) {
        AssignSubmit entity = assignSubmitDao.findById(assignSubmitId).orElse(null);
        return entity == null ? null
            : new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
                entity.getStudent(), entity.getAnswer(), entity.getSubmitAt());
    }

    public List<AssignSubmitDto> getAll() {
        return assignSubmitDao.findAll().stream().map(
            entity -> new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
                entity.getStudent(), entity.getAnswer(), entity.getSubmitAt())).toList();
    }

    public boolean submit(AssignSubmitDto assignSubmitDto) {
        LocalDateTime now = assignSubmitDto.getSubmitAt();
        LocalDateTime deadline = assignSubmitDto.getAssignment().getDeadline();

        boolean flag = false;

        if (now.isBefore(deadline)) {
            assignSubmitDao.save(
                new AssignSubmit(assignSubmitDto.getAssignSubmitId(),
                    assignSubmitDto.getAssignment(), assignSubmitDto.getStudent(),
                    assignSubmitDto.getAnswer(), now));

            flag = true;
            return flag;
        }

        return flag;
    }

    public AssignSubmitDto update(AssignSubmitDto assignSubmitDto) {
        AssignSubmit entity = assignSubmitDao.save(
            new AssignSubmit(assignSubmitDto.getAssignSubmitId(), assignSubmitDto.getAssignment(),
                assignSubmitDto.getStudent(), assignSubmitDto.getAnswer(),
                assignSubmitDto.getSubmitAt()));

        return new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(),
            entity.getStudent(), entity.getAnswer(), entity.getSubmitAt());
    }

    public void delete(UUID assignSubmitId) {
        assignSubmitDao.deleteById(assignSubmitId);
    }
}
