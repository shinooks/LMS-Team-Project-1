package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.repository.AssignScoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignScoreService {

    private final AssignScoreDao assignScoreDao;

    @Autowired
    public AssignScoreService(AssignScoreDao assignScoreDao) {
        this.assignScoreDao = assignScoreDao;
    }

    // TODO
}
