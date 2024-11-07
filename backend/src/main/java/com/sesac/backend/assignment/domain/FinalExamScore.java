package com.sesac.backend.assignment.domain;

import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FinalExamScore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalExamScoreId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalExamId", nullable = false)
    private FinalExam finalExam;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;
    @Column(nullable = false)
    private int score;
    @Enumerated
    @Column(nullable = false)
    private Visibility visibility;


    public enum Visibility {
        PUBLIC, PRIVATE
    }
}
