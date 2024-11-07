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
public class MidtermExamScore {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID midtermExamScoreId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midtermExamId", nullable = false)
    private MidtermExam midtermExam;
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
