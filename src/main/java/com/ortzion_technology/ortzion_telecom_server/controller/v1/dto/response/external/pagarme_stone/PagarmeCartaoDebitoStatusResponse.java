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
public class PagarmeCartaoDebitoStatusResponse {

    @JsonProperty("not_authorized")
    private String notAuthorized;

    @JsonProperty("pending")
    private String pending;

    @JsonProperty("captured")
    private String captured;

    @JsonProperty("refunded")
    private String refunded;

    @JsonProperty("error_on_refunding")
    private String errorOnRefunding;

    @JsonProperty("with_error")
    private String withError;

    @JsonProperty("failed")
    private String failed;

}
