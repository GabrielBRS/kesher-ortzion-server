package com.ortzion_technology.ortzion_telecom_server.entity.domain.pagarme;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagarme_webhook_event", schema = "pagarme")
@Getter
@Setter
@NoArgsConstructor
public class PagarmeWebhookEvent implements Serializable {

    @Id
    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public PagarmeWebhookEvent(String eventId, String status, String payload) {
        this.eventId = eventId;
        this.status = status;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
    }

}

