package com.sesac.backend.course.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "강의")
@Getter @Setter
@NoArgsConstructor
public class Course {
    @Id
    @Column(name = "강의ID")
    private String courseId;

    @Column(name = "강의코드")
    private String courseCode;

    @Column(name = "강의명")
    private String courseName;

    @Column(name = "학과ID")
    private String departmentId;

    @Column(name = "학점")
    private Integer credit;

    @Column(name = "설명")
    private String description;
}