package com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("holder_name")
    private String holderName;

    @JsonProperty("last_four_digits")
    private String lastFourDigits;
}