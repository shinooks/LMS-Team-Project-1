package com.sesac.backend.evaluation.midtermexam.domain;

import com.sesac.backend.entity.BaseEntity;
import com.sesac.backend.entity.Student;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dongjin
 * 중간고사 제출 도메인
 */

@Getter
@Setter
@Builder
@ToString(exclude = {"midtermExam", "student", "midtermExamAnswers"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class MidtermExamSubmit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID midtermSubmitId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "midtermExamId", nullable = false)
    private MidtermExam midtermExam;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;
    @OneToMany(mappedBy = "midtermExamSubmit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MidtermExamAnswer> midtermExamAnswers = new ArrayList<>();
}
