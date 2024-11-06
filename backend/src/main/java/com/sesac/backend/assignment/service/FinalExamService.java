package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.FinalExam;
import com.sesac.backend.assignment.dto.FinalExamDto;
import com.sesac.backend.assignment.repository.FinalExamDao;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinalExamService {

    private final FinalExamDao finalExamDao;

    @Autowired
    public FinalExamService(FinalExamDao finalExamDao) {
        this.finalExamDao = finalExamDao;
    }

    public FinalExamDto getByExamId(UUID finalExamId) {
        return finalExamDao.findById(finalExamId).stream()
            .map(this::convertToDto).findFirst()
            .orElseThrow(RuntimeException::new);
    }

    public List<FinalExamDto> getAllFinalExams() {
        return finalExamDao.findAll().stream().map(this::convertToDto).toList();
    }

    public FinalExamDto createFinalExam(FinalExamDto finalExamDto) {
        return convertToDto(finalExamDao.save(convertToEntity(finalExamDto)));
    }

    public FinalExamDto updateFinalExam(FinalExamDto finalExamDto) {
        FinalExam saved = finalExamDao.findById(finalExamDto.getFinalExamId())
            .orElseThrow(RuntimeException::new);
        saved.setStartTime(finalExamDto.getStartTime());
        saved.setEndTime(finalExamDto.getEndTime());
        return convertToDto(finalExamDao.save(saved));
    }

    public void deleteFinalExam(UUID finalExamId) {
        finalExamDao.deleteById(finalExamId);
    }

    private FinalExamDto convertToDto(FinalExam entity) {
        return new FinalExamDto(entity.getFinalExamId(), entity.getCourse(),
            entity.getStartTime(), entity.getEndTime());
    }

    private FinalExam convertToEntity(FinalExamDto dto) {
        return new FinalExam(dto.getFinalExamId(), dto.getCourse(), dto.getStartTime(),
            dto.getEndTime());
    }
}
