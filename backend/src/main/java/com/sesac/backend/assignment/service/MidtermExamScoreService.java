package com.sesac.backend.assignment.service;

import com.sesac.backend.assignment.repository.MidtermExamScoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MidtermExamScoreService {

    private final MidtermExamScoreDao midtermExamScoreDao;

    @Autowired
    public MidtermExamScoreService(MidtermExamScoreDao midtermExamScoreDao) {
        this.midtermExamScoreDao = midtermExamScoreDao;
    }
}
