package com.sesac.backend.assignment.domain;

import com.sesac.backend.course.domain.Course;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID assignId;
    @ManyToOne
    private Course course;
    private String title;
    private String description;
}
