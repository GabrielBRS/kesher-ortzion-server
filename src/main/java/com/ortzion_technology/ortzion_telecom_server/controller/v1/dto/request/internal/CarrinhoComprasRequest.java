package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoComprasRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multicontaRequestDTO;

    private FormaDePagamento formaDePagamento;

    private List<ItensCarrinho> itensCarrinho;


    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormaDePagamento{

        private Integer idFormaPagamento;

        private Pix pix;
        private Cartao cartao;
        private Boleto boleto;

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Pix {

            private String chavePix;

        }

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Cartao {

            private String cvv;
            private String validade;
            private String nomeTitularCartao;
            private String numeroCartao;
            private String parcelas;

        }

        @Getter
        @Setter
        @ToString
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Boleto {

            private String codigoBoleto;

        }

    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItensCarrinho{

        private String idPacoteCanalMensageria;
        private String quantidadeMercadoriaVirtual;

    }

}
