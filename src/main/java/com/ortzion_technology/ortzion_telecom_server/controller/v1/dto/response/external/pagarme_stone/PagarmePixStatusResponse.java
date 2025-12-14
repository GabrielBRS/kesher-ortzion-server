package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.external.pagarme_stone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class PagarmePixStatusResponse {

    @JsonProperty("waiting_payment")
    private String waitingPayment;

    @JsonProperty("paid")
    private String paid;

    @JsonProperty("pending_refund")
    private String pendingRefund;

    @JsonProperty("refunded")
    private String refunded;

    @JsonProperty("with_error")
    private String withError;

    @JsonProperty("failed")
    private String failed;

}
