package com.sesac.backend.enrollment.domain.classEnrollment;

import com.sesac.backend.enrollment.domain.tempClasses.Classes;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "class_name"}))
public class ClassEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long enrollmentId;

    private String studentId; // 일단 등록할 학생 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Classes classes;

    @Column(nullable = false)
    private String className; // unique 제약 설정

    // 생성자 또는 메서드에서 클래스의 className을 설정
    public ClassEnrollment(Classes classes) {
        this.classes = classes;
        this.className = classes.getClassName(); // Classes 엔터티의 className으로 설정

    }
}
