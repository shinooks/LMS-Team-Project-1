package com.sesac.backend.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.backend.entity.Student;
import com.sesac.backend.notification.domain.Notification;
import com.sesac.backend.notification.domain.NotificationType;
import com.sesac.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public List<Notification> getNotificationsByStudentId(Student student) {
        return notificationRepository.findByStudent(student);
    }

    public void sendNotification(Student student, String title, String content, NotificationType type) {
        try {
            Notification notification = new Notification();
            notification.setStudent(student);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(type);
            notificationRepository.save(notification);

            Map<String, String> notificationData = Map.of(
                "title", title,
                "content", content,
                "type", type.name()
            );

            String message = objectMapper.writeValueAsString(notificationData);
            redisTemplate.convertAndSend("notification:" + student.getStudentId(), message);

        } catch (Exception e) {
            log.error("알림 전송 실패", e);
        }
    }

    public SseEmitter subscribe(Student student) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(student.getStudentId().toString(), emitter);

        emitter.onCompletion(() -> emitters.remove(student.getStudentId().toString()));
        emitter.onTimeout(() -> emitters.remove(student.getStudentId().toString()));

        redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            connection.subscribe((message, pattern) -> {
                try {
                    String payload = String.valueOf(message);
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(payload));
                } catch (IOException e) {
                    emitters.remove(student.getStudentId().toString());
                    log.error("SSE 메시지 전송 실패", e);
                }
            }, ("notification:" + student.getStudentId()).getBytes());
            return null;
        });

        return emitter;
    }
}