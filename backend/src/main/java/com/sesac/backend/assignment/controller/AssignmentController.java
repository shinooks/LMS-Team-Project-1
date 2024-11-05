package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.dto.AssignmentDto;
import com.sesac.backend.assignment.service.AssignmentService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignments/detail")
@RestController
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("")
    public List<AssignmentDto> getAllAssignments() {
        return assignmentService.getAll();
    }

    @GetMapping("/{id}")
    public Map<String, AssignmentDto> getAssignmentById(@PathVariable UUID id) {
        return Map.of("success", assignmentService.findById(id));
    }

    @PostMapping("")
    public Map<String, Boolean> addAssignment(AssignmentDto assignmentDto) {
        boolean flag = false;
        AssignmentDto saved = assignmentService.save(assignmentDto);

        if (saved != null) {
            flag = true;
            return Map.of("success", flag);
        }

        return Map.of("success", flag);
    }

    @PutMapping("")
    public Map<String, Boolean> updateAssignment(AssignmentDto assignmentDto) {
        boolean flag = false;

        try {
            AssignmentDto saved = assignmentService.findById(assignmentDto.getAssignId());
            saved.setCourse(assignmentDto.getCourse());
            saved.setTitle(assignmentDto.getTitle());
            saved.setDescription(assignmentDto.getDescription());
            assignmentService.save(saved);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            return Map.of("success", flag);
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteAssignment(@PathVariable("id") UUID id) {
        boolean flag = false;

        try {
            assignmentService.delete(id);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            return Map.of("success", flag);
        }
    }
}
