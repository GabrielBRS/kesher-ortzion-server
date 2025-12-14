package com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LastTransactionDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("acquirer_message")
    private String acquirerMessage;

    @JsonProperty("card")
    private CardDTO card;

    @JsonProperty("transaction_type")
    private String transactionType;

    @JsonProperty("qr_code_url")
    private String qrCodeUrl;

    @JsonProperty("qr_code")
    private String qrCode;

    @JsonProperty("installments")
    private Integer installments;

    @JsonProperty("url")
    private String url;

    @JsonProperty("pdf")
    private String pdf;

    @JsonProperty("additional_information")
    private List<Map<String, String>> additionalInformation;

}
