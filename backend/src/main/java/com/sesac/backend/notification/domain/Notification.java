package com.sesac.backend.notification.domain;


import com.sesac.backend.entity.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue
    private UUID notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId", nullable = false)
    private Student student;  // Student 엔티티 참조

    @Column(nullable = false)
    private String title;      // 알림 제목

    @Column(nullable = false)
    private String content;    // 알림 내용

    @Enumerated(EnumType.STRING)
    private NotificationType type = NotificationType.GRADE;  // 알림 유형

    private boolean isRead = false;  // 읽음 여부

    private LocalDateTime createdAt;  // 생성 시간

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
