package com.sesac.backend.grade.service;

import com.sesac.backend.entity.AppealStatus;
import com.sesac.backend.entity.Grade;
import com.sesac.backend.entity.GradeAppeal;
import com.sesac.backend.grade.dto.GradeAppealDto;
import com.sesac.backend.grade.dto.GradeAppealRequest;
import com.sesac.backend.grade.dto.GradeUpdateRequest;
import com.sesac.backend.grade.repository.GradeAppealRepository;
import com.sesac.backend.grade.repository.GradeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeAppealService {
    private final GradeRepository gradeRepository;
    private final GradeAppealRepository appealRepository;
    private final GradeService gradeService;

    @Transactional
    public void createAppeal(GradeAppealRequest request) {
        if (!gradeService.isGradeVisible(request.getGradeId())) {
            throw new IllegalStateException("성적 공개 기간이 아닙니다.");
        }

        Grade grade = gradeRepository.findById(request.getGradeId())
                .orElseThrow(() -> new EntityNotFoundException("해당 성적이 존재하지 않습니다."));

        GradeAppeal appeal = new GradeAppeal();
        appeal.setGrade(grade);
        appeal.setContent(request.getContent());
        appeal.setRequestedAssignScore(request.getRequestedAssignScore());
        appeal.setRequestedMidtermScore(request.getRequestedMidtermScore());
        appeal.setRequestedFinalScore(request.getRequestedFinalScore());
        appealRepository.save(appeal);
    }

    @Transactional
    public void processAppeal(UUID appealId, boolean approved) {
        GradeAppeal appeal = appealRepository.findById(appealId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이의신청이 존재하지 않습니다."));

        if(approved) {
            appeal.setStatus(AppealStatus.APPROVED);
            GradeUpdateRequest updateRequest = new GradeUpdateRequest();
            updateRequest.setGradeId(appeal.getGrade().getGradeId());
            updateRequest.setAssignScore(appeal.getRequestedAssignScore());
            updateRequest.setMidtermScore(appeal.getRequestedMidtermScore());
            updateRequest.setFinalScore(appeal.getRequestedFinalScore());

            gradeService.updateGradeScores(updateRequest);
        } else {
            appeal.setStatus(AppealStatus.REJECTED);
        }
    }

    public List<GradeAppealDto> getAppealsByGrade(UUID gradeId) {
        List<GradeAppeal> appeals = appealRepository.findByGrade_GradeId(gradeId);
        return appeals.stream()
                .map(GradeAppealDto::from)
                .collect(Collectors.toList());
    }

    public List<GradeAppealDto> getAllAppeals() {
        List<GradeAppeal> appeals = appealRepository.findAll();
        return appeals.stream()
                .map(GradeAppealDto::from)
                .collect(Collectors.toList());
    }
}

