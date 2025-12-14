package com.ortzion_technology.ortzion_telecom_server.entity.dto.pingoo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SistelcomResponseDTO {

    private String code;
    private String description;
    private String hash;

    @JsonProperty("externalid")
    private String externalId;

}
