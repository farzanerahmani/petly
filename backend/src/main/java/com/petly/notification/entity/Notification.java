package com.petly.notification.entity;

import com.petly.common.entity.BaseEntity;
import com.petly.notification.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 10/29/2026
 */
@Entity
@Table(name = "user_notifications")
@Getter
@Setter
public class Notification extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 40)
    private NotificationType notificationType;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "dedupe_key", nullable = false, length = 300)
    private String dedupeKey;

    @Column(name = "read_at")
    private Instant readAt;

    public boolean isRead() {
        return readAt != null;
    }
}

