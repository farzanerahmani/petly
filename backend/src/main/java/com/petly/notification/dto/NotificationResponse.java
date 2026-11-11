package com.petly.notification.dto;

import com.petly.notification.entity.Notification;
import com.petly.notification.entity.enums.NotificationType;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 10/29/2026
 */
public record NotificationResponse(
        Long id,
        NotificationType type,
        String title,
        String body,
        boolean read,
        Instant createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getNotificationType(),
                notification.getTitle(),
                notification.getBody(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
