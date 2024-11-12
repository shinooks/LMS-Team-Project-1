package com.sesac.backend.evaluation.exam.controller;

import com.sesac.backend.evaluation.exam.dto.ExamCreationRequest;
import com.sesac.backend.evaluation.exam.dto.ExamProblemDto;
import com.sesac.backend.evaluation.exam.dto.ExamSubmissionRequest;
import com.sesac.backend.evaluation.exam.service.ExamService;
import com.sesac.backend.evaluation.score.domain.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * 새로운 시험 출제
     *
     * @param examCreationRequest 시험 정보
     * @return 생성된 시험 정보
     */
    @PostMapping("")
    public ResponseEntity<ExamCreationRequest> createExam(ExamCreationRequest examCreationRequest) {
        ExamCreationRequest createdExam = examService.createExam(examCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
    }

    /**
     * 특정 시험 조회
     *
     * @param examId 조회할 시험 ID
     * @return 조회된 시험 정보
     */
    @GetMapping("/{examId}")
    public ResponseEntity<ExamCreationRequest> getExam(@PathVariable UUID examId) {
        ExamCreationRequest exam = examService.getByExamId(examId);
        return ResponseEntity.ok(exam);
    }

    /**
     * 전체 시험 조회
     *
     * @return 모든 시험 리스트
     */
    @GetMapping("")
    public ResponseEntity<List<ExamCreationRequest>> getAllExams() {
        List<ExamCreationRequest> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    /**
     * 특정 시험에 문제 추가
     *
     * @param request
     * @return 추가된 문제 리스트
     */
    @PostMapping("/{examId}/problems")
    public ResponseEntity<ExamCreationRequest> saveExamProblems(ExamCreationRequest request) {
        ExamCreationRequest examCreationRequest = examService.saveExamProblems(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(examCreationRequest);
    }

    /**
     * 특정 시험 문제 조회
     *
     * @param examId 조회할 시험 ID
     * @return 시험 문제 리스트
     */
    @GetMapping("/{examId}/problems")
    public ResponseEntity<List<ExamProblemDto>> getExamProblems(@PathVariable UUID examId) {
        List<ExamProblemDto> problems = examService.findAllExamProblems(examId);
        return ResponseEntity.ok(problems);
    }

    /**
     * 시험 응시 및 채점
     *
     * @param examSubmissionRequest
     * @return 채점된 점수
     */
    @PostMapping("/{examId}/submit")
    public ResponseEntity<ExamSubmissionRequest> submitExam(ExamSubmissionRequest examSubmissionRequest) {
        ExamSubmissionRequest request = examService.submit(examSubmissionRequest);
        return ResponseEntity.ok(request);
    }

    /**
     * 시험 정보 업데이트
     *
     * @param examId 시험 ID
     * @param examCreationRequest 수정할 시험 정보
     * @return 수정된 시험 정보
     */
    @PutMapping("/{examId}")
    public ResponseEntity<ExamCreationRequest> updateExam(
        @PathVariable UUID examId,
        @RequestBody ExamCreationRequest examCreationRequest) {
        examCreationRequest.setExamId(examId); // 수정할 시험 ID 설정
        ExamCreationRequest updatedExam = examService.updateExam(examCreationRequest);
        return ResponseEntity.ok(updatedExam);
    }

    /**
     * 시험 삭제
     *
     * @param examId 삭제할 시험 ID
     * @return 상태 코드 반환
     */
    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable UUID examId) {
        examService.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }
}
