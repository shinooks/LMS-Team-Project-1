package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.service.AssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignment/submit")
@RestController
public class AssignSubmitController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignSubmitController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // TODO
}
