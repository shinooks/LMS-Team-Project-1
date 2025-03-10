package com.sesac.backend.evaluation.copyleaks.controller;

import com.sesac.backend.evaluation.assignment.repository.AssignmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/copyleaks")
@Tag(name = "표절 검사 응답 API", description = "표절검사 응답 엔드포인트")
public class CopyleaksWebhookController {

    private final AssignmentRepository assignmentRepository;

    @Autowired
    public CopyleaksWebhookController(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @PostMapping("")
    @Operation(summary = "copyleaks 결과 hook", description = "결과(payload)")
    public ResponseEntity<String> receiveResult(@RequestBody Map<String, Object> payload) {
        // 결과 처리 로직
        // payload에서 scanId, 결과 URL 등을 추출하여 처리

        log.info("Received Copyleaks result: {}", payload);
//        Assignment assignment = assignmentRepository.findByScanId((UUID) payload.get("id"));
//        assignment.setCopyleaksResult(payload.toString());

        return ResponseEntity.ok("Received");
    }
}
