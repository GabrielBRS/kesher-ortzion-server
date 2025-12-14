package com.ortzion_technology.ortzion_telecom_server.repository.pagarme;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.pagarme.PagarmeWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagarmeWebhookEventRepository extends JpaRepository<PagarmeWebhookEvent, String> {
}
