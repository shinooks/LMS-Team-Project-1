package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.dto.AssignmentsDto;
import com.sesac.backend.assignment.service.AssignmentsService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignments")
@RestController
public class AssignmentsController {

    private final AssignmentsService assignmentsService;

    public AssignmentsController(AssignmentsService assignmentsService) {
        this.assignmentsService = assignmentsService;
    }

    @GetMapping("")
    public List<AssignmentsDto> getAllAssignments() {
        return assignmentsService.getAll();
    }

    @GetMapping("/{id}")
    public Map<String, AssignmentsDto> getAssignmentById(@PathVariable long id) {
        return Map.of("assignment", assignmentsService.findById(id));
    }

    @PostMapping("/")
    public Map<String, AssignmentsDto> addAssignment(AssignmentsDto assignment) {
        AssignmentsDto saved = assignmentsService.save(assignment);

        return Map.of("assignment", new AssignmentsDto(
            saved.getId(), saved.getCourseDummy(), saved.getTitle(), saved.getDescription()
        ));
    }

    @PutMapping()
    public Map<String, AssignmentsDto> updateAssignment(AssignmentsDto assignment) {
        AssignmentsDto saved = assignmentsService.findById(assignment.getId());
        saved.setCourseDummy(assignment.getCourseDummy());
        saved.setTitle(assignment.getTitle());
        saved.setDescription(assignment.getDescription());
        assignmentsService.save(saved);
        return Map.of("assignment", new AssignmentsDto(
            saved.getId(), saved.getCourseDummy(), saved.getTitle(), saved.getDescription()
        ));
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteAssignment(@PathVariable long id) {
        assignmentsService.delete(id);
        return Map.of("deleted", true);
    }
}
