package com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagarmeResponseDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("status")
    private String status;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("charges")
    private List<ChargeDTO> charges;

    @JsonProperty("customer")
    private CustomerDTO customer;

}