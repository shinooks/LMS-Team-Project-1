package com.sesac.backend.grade.service;

import com.sesac.backend.entity.Grade;
import com.sesac.backend.grade.repository.GradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {
    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeService gradeService;
}