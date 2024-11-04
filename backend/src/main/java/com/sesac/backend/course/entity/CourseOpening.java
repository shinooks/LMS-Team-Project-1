package com.sesac.backend.course.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "강의개설")
@Getter @Setter
@NoArgsConstructor
public class CourseOpening {
    @Id
    @Column(name = "개설ID")
    private String openingId;

    @Column(name = "강의ID")
    private String courseId;

    @Column(name = "교수ID")
    private String professorId;

    @Column(name = "학기")
    private String semester;

    @Column(name = "연도")
    private Integer year;

    @Column(name = "최대수강인원")
    private Integer maxStudents;

    @Column(name = "현재수강인원")
    private Integer currentStudents;

    @Column(name = "상태")
    private String status;

    @OneToMany(mappedBy = "courseOpening")
    private List<CourseTime> courseTimes;

    @OneToOne(mappedBy = "courseOpening")
    private SyllaBus syllabus;

    @ManyToOne
    @JoinColumn(name = "강의ID", insertable = false, updatable = false)
    private Course course;
}