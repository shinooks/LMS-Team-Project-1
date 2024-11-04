package com.sesac.backend.course.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CourseDummy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
