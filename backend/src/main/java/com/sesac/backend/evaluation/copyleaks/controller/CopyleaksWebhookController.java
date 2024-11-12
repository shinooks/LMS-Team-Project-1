package com.sesac.backend.evaluation.copyleaks.controller;

import com.sesac.backend.evaluation.assignment.domain.Assignment;
import com.sesac.backend.evaluation.assignment.repository.AssignmentRepository;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/copyleaks")
public class CopyleaksWebhookController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @PostMapping("")
    public ResponseEntity<String> receiveResult(@RequestBody Map<String, Object> payload) {
        // 결과 처리 로직
        // payload에서 scanId, 결과 URL 등을 추출하여 처리

        log.info("Received Copyleaks result: {}", payload);
//        Assignment assignment = assignmentRepository.findByScanId((UUID) payload.get("id"));
//        assignment.setCopyleaksResult(payload.toString());

        return ResponseEntity.ok("Received");
    }
}
