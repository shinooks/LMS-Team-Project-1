package com.sesac.backend.notification;

import com.sesac.backend.notification.domain.Notification;
import com.sesac.backend.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private UUID notificationId;
    private String title;
    private String content;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(
                notification.getNotificationId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}