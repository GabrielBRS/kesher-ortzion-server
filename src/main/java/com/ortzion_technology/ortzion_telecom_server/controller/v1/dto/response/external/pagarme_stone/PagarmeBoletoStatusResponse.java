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
public class PagarmeBoletoStatusResponse {

    @JsonProperty("generated")
    private String generated;

    @JsonProperty("viewed")
    private String viewed;

    @JsonProperty("underpaid")
    private String underpaid;

    @JsonProperty("overpaid")
    private String overpaid;

    @JsonProperty("paid")
    private String paid;

    @JsonProperty("voided")
    private String voided;

    @JsonProperty("with_error")
    private String withError;

    @JsonProperty("failed")
    private String failed;

    @JsonProperty("processing")
    private String processing;
    
}
