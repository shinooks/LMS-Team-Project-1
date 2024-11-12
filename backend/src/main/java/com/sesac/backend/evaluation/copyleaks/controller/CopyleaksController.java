package com.sesac.backend.evaluation.copyleaks.controller;

import com.sesac.backend.evaluation.copyleaks.service.CopyleaksService;
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
public class CopyleaksController {

    @Autowired
    private CopyleaksService copyleaksService;

    @PostMapping("/check/{assignId}")
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
