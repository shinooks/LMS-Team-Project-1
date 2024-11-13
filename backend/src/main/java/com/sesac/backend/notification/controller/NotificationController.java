package com.sesac.backend.notification.controller;

import com.sesac.backend.entity.Student;
import com.sesac.backend.notification.domain.Notification;
import com.sesac.backend.notification.service.NotificationService;
import com.sesac.backend.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final StudentService studentService;

    @GetMapping("/subscribe/{studentId}")
    public SseEmitter subscribe(@PathVariable UUID studentId) {
        Student student = studentService.findById(studentId);
        return notificationService.subscribe(student);
    }

    @GetMapping("/student/{studentId}")
    public List<Notification> getStudentNotifications(@PathVariable UUID studentId) {
        Student student = studentService.findById(studentId);
        return notificationService.getNotificationsByStudentId(student);
    }
}