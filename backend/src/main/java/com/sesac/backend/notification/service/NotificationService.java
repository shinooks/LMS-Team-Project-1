package com.sesac.backend.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.backend.entity.Student;
import com.sesac.backend.notification.NotificationDto;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public List<NotificationDto> getNotificationsByStudentId(Student student) {
        return notificationRepository.findByStudent(student)
            .stream()
            .map(NotificationDto::from)
            .collect(Collectors.toList());
    }

    public void sendNotification(Student student, String title, String content, NotificationType type) {
        try {
            // DB에 알림 저장
            Notification notification = new Notification();
            notification.setStudent(student);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(type);
            notificationRepository.save(notification);

            // SSE로 직접 전송
            SseEmitter emitter = emitters.get(student.getStudentId().toString());
            if (emitter != null) {
                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("title", title);
                notificationData.put("content", content);
                notificationData.put("type", type.name());
                notificationData.put("timestamp", LocalDateTime.now());

                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(objectMapper.writeValueAsString(notificationData)));
            }
        } catch (Exception e) {
            log.error("알림 전송 실패", e);
        }
    }


    public SseEmitter subscribe(Student student) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(student.getStudentId().toString(), emitter);

        emitter.onCompletion(() -> emitters.remove(student.getStudentId().toString()));
        emitter.onTimeout(() -> emitters.remove(student.getStudentId().toString()));

        // 연결 확인을 위한 더미 이벤트 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            emitter.complete();
            emitters.remove(student.getStudentId().toString());
            log.error("SSE 연결 실패", e);
        }

        return emitter;
    }
}