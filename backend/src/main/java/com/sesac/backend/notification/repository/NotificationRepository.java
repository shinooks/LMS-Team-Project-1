package com.sesac.backend.notification.repository;

import com.sesac.backend.entity.Student;
import com.sesac.backend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByStudent(Student student);
}
