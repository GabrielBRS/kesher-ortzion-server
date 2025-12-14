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
public class PagarmeCartaoCreditoStatusResponse {

    @JsonProperty("authorized_pending_capture")
    private String authorizedPendingCapture;

    @JsonProperty("not_authorized")
    private String notAuthorized;

    @JsonProperty("captured")
    private String captured;

    @JsonProperty("partial_capture")
    private String partialCapture;

    @JsonProperty("waiting_capture")
    private String waitingCapture;

    @JsonProperty("refunded")
    private String refunded;

    @JsonProperty("voided")
    private String voided;

    @JsonProperty("partial_refunded")
    private String partialRefunded;

    @JsonProperty("partial_void")
    private String partialVoid;

    @JsonProperty("error_on_voiding")
    private String errorOnVoiding;

    @JsonProperty("error_on_refunding")
    private String errorOnRefunding;

    @JsonProperty("waiting_cancellation")
    private String waitingCancellation;

    @JsonProperty("with_error")
    private String withError;

    @JsonProperty("failed")
    private String failed;

}
