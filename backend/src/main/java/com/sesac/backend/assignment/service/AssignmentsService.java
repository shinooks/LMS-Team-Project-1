package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.AssignmentsDo;
import com.sesac.backend.assignment.dto.AssignmentsDto;
import com.sesac.backend.assignment.repository.AssignmentsDao;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AssignmentsService {

    private final AssignmentsDao assignmentsDao;

    public AssignmentsService(AssignmentsDao assignmentsDao) {
        this.assignmentsDao = assignmentsDao;
    }

    public AssignmentsDto save(AssignmentsDto assignmentsDto) {
        AssignmentsDo assignmentsDo = assignmentsDao.save(
            new AssignmentsDo(assignmentsDto.getId(), assignmentsDto.getCourseDummy(),
                assignmentsDto.getTitle(), assignmentsDto.getTitle()));

        return new AssignmentsDto(assignmentsDo.getId(), assignmentsDo.getCourseDummy(),
            assignmentsDo.getTitle(), assignmentsDo.getTitle());
    }

    public List<AssignmentsDto> getAll() {
        return assignmentsDao.findAll().stream().map(entity -> new AssignmentsDto(entity.getId(),
            entity.getCourseDummy(), entity.getTitle(), entity.getDescription())).toList();
    }

    public AssignmentsDto findById(Long id) {
        AssignmentsDo entity = assignmentsDao.findById(id).orElse(null);

        return entity == null ? null
            : new AssignmentsDto(entity.getId(), entity.getCourseDummy(), entity.getTitle(),
                entity.getDescription());
    }

    public void delete(Long id) {
        assignmentsDao.deleteById(id);
    }
}
