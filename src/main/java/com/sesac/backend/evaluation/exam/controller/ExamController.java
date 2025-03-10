package com.sesac.backend.evaluation.exam.controller;

import com.sesac.backend.evaluation.exam.dto.request.ExamCreationRequest;
import com.sesac.backend.evaluation.exam.dto.request.ExamRequest;
import com.sesac.backend.evaluation.exam.dto.request.ExamSubmissionRequest;
import com.sesac.backend.evaluation.exam.dto.response.ExamCreationResponse;
import com.sesac.backend.evaluation.exam.dto.response.ExamReadResponse;
import com.sesac.backend.evaluation.exam.dto.response.ExamResponse;
import com.sesac.backend.evaluation.exam.service.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/exams")
@Tag(name = "시험 관리 API", description = "시험 생성, 응시, 제출, 채점 관련 엔드포인트")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * 시험 출제
     *
     * @param request 시험 정보
     * @return 생성된 시험 정보
     */
    @PostMapping("")
    @Operation(summary = "시험 출제", description = "시험아이디(examId), 강의개설아이디(openingId), 학생아이디(studentId), 문제목록(problems[problemId, number, correctAnswer, difficulty, question, choices]), 시험타입(Type), 시작시간(startTime), 종료시간(endTime)")
    public ResponseEntity<ExamCreationResponse> createExam(@RequestBody ExamCreationRequest request) {
        try {
            ExamCreationResponse createdExam = examService.createExam(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 시험 목록 조회
     *
     * @param openingId 수강신청아이디
     * @param studentId 학생아이디
     * @return 응시할 시험
     */
    @GetMapping("/{openingId}/{studentId}")
    private ResponseEntity<List<ExamResponse>> getExamProblems(
        @PathVariable UUID openingId,
        @PathVariable UUID studentId
    ) {
        try {
            List<ExamResponse> responses = examService.getMyExam(new ExamRequest(openingId, studentId));
            return ResponseEntity.ok().body(responses);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    /**
     * 시험 문제 조회
     *
     * @param examId 조회할 시험 ID
     * @return 조회된 시험 정보
     */
    @Operation(summary = "시험 문제 조회", description = "시험아이디(examId)")
    @GetMapping("/{examId}")
    public ResponseEntity<ExamReadResponse> getExamProblems(@PathVariable UUID examId) {
        try {
            ExamReadResponse exam = examService.getMyExamProblems(examId);
            return ResponseEntity.ok(exam);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 시험 응시 및 채점
     *
     * @param examSubmissionRequest
     * @return 채점된 점수
     */
    @Operation(summary = "시험 응시", description = "시험아이디(examId), 학생아이디(studentId), 응답(answers[problemId, number, selectedAnswer])")
    @PostMapping("/submit")
    public ResponseEntity<ExamSubmissionRequest> submitExam(
        @RequestBody ExamSubmissionRequest examSubmissionRequest) {
        ExamSubmissionRequest request = examService.submit(examSubmissionRequest);
        return ResponseEntity.ok(request);
    }
}
