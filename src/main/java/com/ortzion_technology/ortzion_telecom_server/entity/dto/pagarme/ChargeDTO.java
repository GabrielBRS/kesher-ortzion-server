package com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("status")
    private String status;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("paid_amount")
    private Integer paidAmount;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("customer")
    private CustomerDTO customer;

    @JsonProperty("last_transaction")
    private LastTransactionDTO lastTransaction;

}
