package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.domain.AssignSubmit;
import com.sesac.backend.assignment.dto.AssignSubmitDto;
import com.sesac.backend.assignment.service.AssignmentService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("")
    public List<AssignSubmitDto> getAllSubmits() {
        return List.of();
    }

    @GetMapping("/{id}")
    public Map<String, AssignSubmitDto> getById(@PathVariable("id") UUID id) {
        return Map.of("success", new AssignSubmitDto());
    }

    @PostMapping("")
    public Map<String, Boolean> submit(AssignSubmitDto assignSubmitDto) {
        return Map.of("success", false);
    }
}
