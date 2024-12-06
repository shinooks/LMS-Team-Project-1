package com.sesac.backend.evaluation.copyleaks.controller;

import com.sesac.backend.evaluation.copyleaks.service.CopyleaksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/copyleaks")
@Tag(name = "표절 검사 요청 API", description = "표절 검사 요청 엔드포인트")
public class CopyleaksController {

    private final CopyleaksService copyleaksService;

    @Autowired
    public CopyleaksController(CopyleaksService copyleaksService) {
        this.copyleaksService = copyleaksService;
    }

    @PostMapping("/check/{assignId}")
    @Operation(summary = "copyleaks 요청", description = "과제아이디(assignId)")
    public ResponseEntity<String> checkPlagiarism(@PathVariable UUID assignId) {
        try {
            copyleaksService.checkPlagiarism(assignId);
            return ResponseEntity.ok("Plagiarism check initiated.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error initiating plagiarism check: " + e.getMessage());
        }
    }
}
