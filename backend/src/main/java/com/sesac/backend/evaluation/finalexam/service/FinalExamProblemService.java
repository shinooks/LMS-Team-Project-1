//package com.sesac.backend.evaluation.finalexam.service;
//
//import com.sesac.backend.evaluation.finalexam.domain.FinalExam;
//import com.sesac.backend.evaluation.finalexam.domain.FinalExamProblem;
//import com.sesac.backend.evaluation.finalexam.dto.FinalExamProblemDto;
//import com.sesac.backend.evaluation.finalexam.repository.FinalExamProblemRepository;
//import com.sesac.backend.evaluation.finalexam.repository.FinalExamRepository;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FinalExamProblemService {
//
//    private final FinalExamProblemRepository finalExamProblemRepository;
//    private final FinalExamRepository finalExamRepository;
//
//    @Autowired
//    public FinalExamProblemService(FinalExamProblemRepository finalExamProblemRepository, FinalExamRepository finalExamRepository) {
//        this.finalExamProblemRepository = finalExamProblemRepository;
//        this.finalExamRepository = finalExamRepository;
//    }
//
//    public FinalExamProblem createFinalExam(FinalExamProblemDto dto, UUID finalExamId) {
//        // FinalExam 조회
//        FinalExam finalExam = finalExamRepository.findById(finalExamId)
//            .orElseThrow(() -> new RuntimeException("FinalExam not found with id: " + finalExamId));
//
//        // 현재 최대 number 값 조회
//        Integer maxNumber = finalExamProblemRepository.findMaxNumberByFinalExam(finalExam);
//        int nextNumber = (maxNumber != null) ? maxNumber + 1 : 1;
//
//        // 새 문제 생성 및 초기화
//        FinalExamProblem newProblem = new FinalExamProblem();
//        newProblem.setFinalExam(finalExam);
//        newProblem.setNumber(nextNumber);
//        newProblem.setCorrectAnswer(dto.getCorrectAnswer());
//        newProblem.setDifficulty(dto.getDifficulty());
//        newProblem.setQuestion(dto.getQuestion());
//        newProblem.setChoices(dto.getChoices());
//
//        return finalExamProblemRepository.save(newProblem);
//    }
//}
