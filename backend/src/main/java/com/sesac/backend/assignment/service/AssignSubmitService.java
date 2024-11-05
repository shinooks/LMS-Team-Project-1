package com.sesac.backend.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignSubmitService {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignSubmitService(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // TODO
}
