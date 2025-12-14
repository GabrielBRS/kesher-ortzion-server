package com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPagarmeResponseDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("data")
    private OrderDataDTO data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderDataDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("code")
        private String code;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("status")
        private String status;

        @JsonProperty("items")
        private List<ItemDTO> items;

        @JsonProperty("customer")
        private CustomerDTO customer;

        @JsonProperty("charges")
        private List<ChargeDTO> charges;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("description")
        private String description;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("quantity")
        private Integer quantity;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("document")
        private String document;

        @JsonProperty("type")
        private String type;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChargeDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("code")
        private String code;

        @JsonProperty("amount")
        private Integer amount;

        @JsonProperty("status")
        private String status;

        @JsonProperty("payment_method")
        private String paymentMethod;

        @JsonProperty("paid_at")
        private String paidAt;

        @JsonProperty("last_transaction")
        private LastTransactionDTO lastTransaction;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LastTransactionDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("transaction_type")
        private String transactionType;

        @JsonProperty("status")
        private String status;

        @JsonProperty("success")
        private boolean success;

        // Campos específicos de PIX
        @JsonProperty("qr_code")
        private String qrCode;

        @JsonProperty("qr_code_url")
        private String qrCodeUrl;

        // Campos específicos de Cartão
        @JsonProperty("installments")
        private Integer installments;

        @JsonProperty("card")
        private CardDTO card;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CardDTO {
        @JsonProperty("id")
        private String id;

        @JsonProperty("last_four_digits")
        private String lastFourDigits;

        @JsonProperty("brand")
        private String brand;

        @JsonProperty("holder_name")
        private String holderName;
    }

}