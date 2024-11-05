package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.AssignSubmit;
import com.sesac.backend.assignment.dto.AssignScoreDto;
import com.sesac.backend.assignment.dto.AssignSubmitDto;
import com.sesac.backend.assignment.repository.AssignSubmitDao;
import java.util.List;
import java.util.UUID;
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

    public List<AssignSubmitDto> findAll() {
        return assignSubmitDao.findAll().stream().map(entity -> new AssignSubmitDto(entity.getAssignSubmitId(), entity.getAssignment(), entity.getStudent(),
            entity.getAnswer(), ,))
    }
}
