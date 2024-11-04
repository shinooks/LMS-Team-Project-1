package com.sesac.backend.assignment.domain;

import com.sesac.backend.course.domain.CourseDummy;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AssignmentsDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private CourseDummy courseDummy;
    private String title;
    private String description;
}
