package com.sesac.backend.evaluation.finalexam.service;

import com.sesac.backend.evaluation.finalexam.repository.FinalExamSubmitRepository;
import org.springframework.stereotype.Service;

@Service
public class FinalExamSubmitService {
    private final FinalExamSubmitRepository finalExamSubmitRepository;

    public FinalExamSubmitService(FinalExamSubmitRepository finalExamSubmitRepository) {
        this.finalExamSubmitRepository = finalExamSubmitRepository;
    }
}
