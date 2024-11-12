package com.sesac.backend.evaluation.exam.controller;

import com.sesac.backend.evaluation.exam.dto.ExamCreationRequest;
import com.sesac.backend.evaluation.exam.dto.ExamProblemDto;
import com.sesac.backend.evaluation.exam.dto.ExamSubmissionRequest;
import com.sesac.backend.evaluation.exam.service.ExamService;
import com.sesac.backend.evaluation.score.domain.Score;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exams")
@Tag(name = "시험 관리 API", description = "시험 생성, 응시, 제출, 채점 관련 엔드포인트")
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
    @Operation(summary = "시험 출제", description = "시험아이디(examId), 강의개설아이디(openingId), 학생아이디(studentId), 문제목록(problems[problemId, number, correctAnswer, difficulty, question, choices]), 시험타입(Type), 시작시간(startTime), 종료시간(endTime)")
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
    @Operation(summary = "시험 조회", description = "시험아이디(examId)")
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
    @Operation(summary = "전체 시험 조회", description = ".")
    @GetMapping("")
    public ResponseEntity<List<ExamCreationRequest>> getAllExams() {
        List<ExamCreationRequest> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    /**
     * 특정 시험 문제 조회
     *
     * @param examId 조회할 시험 ID
     * @return 시험 문제 리스트
     */
    @GetMapping("/{examId}/problems")
    @Operation(summary = "시험 문제 조회", description = "시험아이디(examId)")
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
    @Operation(summary = "시험 응시", description = "시험아이디(examId), 학생아이디(studentId), 응답(answers[problemId, number, selectedAnswer])")
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
