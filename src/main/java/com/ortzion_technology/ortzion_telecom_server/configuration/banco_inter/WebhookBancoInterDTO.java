package com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme.ChargeDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class WebhookBancoInterDTO {

    // --- DTOs PARA PIX (COBRANÇA IMEDIATA) ---

    @Data
    public static class PixCobrancaImediataRequest {
        private Calendario calendario;
        private Devedor devedor;
        private Valor valor;
        private String chave;
        private String solicitacaoPagador;

        @Data
        public static class Calendario {
            private Integer expiracao; // em segundos
        }
        @Data
        public static class Devedor {
            private String cpf;
            private String cnpj;
            private String nome;
        }
        @Data
        public static class Valor {
            private String original;
        }
    }

    @Data
    public static class PixCobrancaImediataResponse {
        private String txid;
        private String pixCopiaECola;
        private Loc loc;
        private String status;

        @Data
        public static class Loc {
            private int id;
            private String location;
            private String tipoCob;
        }
    }

    @Data
    public static class PixResponseFront {
        private String txid;
        private String pixCopiaECola;
        private String qrcodeBase64;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BoletoRequest {
        private String seuNumero;
        private BigDecimal valorNominal;
        private String dataVencimento;
        private Pagador pagador;
        private BigDecimal desconto;
        private BigDecimal multa;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pagador {
        private String tipoPessoa;
        private String nome;
        private String cpfCnpj;
        private String logradouro;
        private String numero;
        private String complemento;
        private String bairro;
        private String cidade;
        private String uf;
        private String cep;
        private String email;
        private String telefone;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class BancoInterResponseDTO {

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

}