package com.sesac.backend.assignment.controller;

import com.sesac.backend.assignment.dto.AssignSubmitDto;
import com.sesac.backend.assignment.service.AssignSubmitService;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/assignment/submit")
@RestController
public class AssignSubmitController {

    private final AssignSubmitService assignSubmitService;

    @Autowired
    public AssignSubmitController(AssignSubmitService assignSubmitService) {
        this.assignSubmitService = assignSubmitService;
    }

    @GetMapping("")
    public List<AssignSubmitDto> getAllSubmits() {
        return assignSubmitService.getAll();
    }

    @GetMapping("/{id}")
    public Map<String, AssignSubmitDto> getById(@PathVariable("id") UUID id) {
        try {
            return Map.of("success", assignSubmitService.findById(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", null);
        }
    }

    @PostMapping("")
    public Map<String, Boolean> submit(AssignSubmitDto assignSubmitDto) {
        boolean flag = false;

        try {
            assignSubmitService.submit(assignSubmitDto);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", flag);
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable("id") UUID id) {
        boolean flag = false;

        try {
            assignSubmitService.delete(id);
            flag = true;
            return Map.of("success", flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("success", flag);
        }
    }
}
