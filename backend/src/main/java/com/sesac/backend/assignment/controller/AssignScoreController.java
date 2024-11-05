package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.service.AssignScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("*")
@RestController
public class AssignScoreController {

    private final AssignScoreService assignScoreService;

    @Autowired
    public AssignScoreController(AssignScoreService assignScoreService) {
        this.assignScoreService = assignScoreService;
    }

    // TODO
}
