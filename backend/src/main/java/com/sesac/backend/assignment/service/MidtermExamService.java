package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.domain.MidtermExam;
import com.sesac.backend.assignment.dto.MidtermExamDto;
import com.sesac.backend.assignment.repository.MidtermExamDao;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MidtermExamService {

    private final MidtermExamDao midtermExamDao;

    public MidtermExamService(MidtermExamDao midtermExamDao) {
        this.midtermExamDao = midtermExamDao;
    }

    public MidtermExamDto getMidtermExam(UUID midtermExamId) {
        return midtermExamDao.findById(midtermExamId).stream()
            .map(this::convertToDto)
            .findFirst().orElseThrow(RuntimeException::new);
    }

    public List<MidtermExamDto> getAllMidtermExams() {
        return midtermExamDao.findAll().stream().map(this::convertToDto).toList();
    }

    public MidtermExamDto createMidtermExam(MidtermExamDto midtermExamDto) {
        return convertToDto(midtermExamDao.save(convertToEntity(midtermExamDto)));
    }

    public MidtermExamDto updateMidtermExam(MidtermExamDto midtermExamDto) {
        MidtermExam saved = midtermExamDao.findById(midtermExamDto.getMidtermExamId())
            .orElseThrow(RuntimeException::new);

        saved.setStartTime(midtermExamDto.getStartTime());
        saved.setEndTime(midtermExamDto.getEndTime());

        return convertToDto(midtermExamDao.save(saved));
    }

    public void deleteMidtermExam(UUID midtermExamId) {
        midtermExamDao.findById(midtermExamId).orElseThrow(RuntimeException::new);
        midtermExamDao.deleteById(midtermExamId);
    }

    private MidtermExamDto convertToDto(MidtermExam entity) {
        return new MidtermExamDto(entity.getMidtermExamId(), entity.getCourse(),
            entity.getStartTime(), entity.getEndTime());
    }

    private MidtermExam convertToEntity(MidtermExamDto dto) {
        return new MidtermExam(dto.getMidtermExamId(), dto.getCourse(), dto.getStartTime(),
            dto.getEndTime());
    }
}
