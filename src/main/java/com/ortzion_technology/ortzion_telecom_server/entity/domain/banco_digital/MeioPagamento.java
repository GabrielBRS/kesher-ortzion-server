package com.ortzion_technology.ortzion_telecom_server.entity.domain.banco_digital;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter.WebhookBancoInterDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme.ChargeDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme.PagarmeResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "meio_pagamento", schema = "banco_digital")
@SequenceGenerator(
        name = "seq_id_meio_pagamento_generator",
        sequenceName = "cadastral.seq_id_meio_pagamento",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeioPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_meio_pagamento")
    private Long idMeioPagamento;

    @Column(name = "forma_pagamento")
    private Integer formaPagamento;

    @Column(name = "status_processamento_pagamento")
    private Integer statusProcessamentoPagamento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "data_processamento_pagamento")
    private LocalDateTime dataProcessamentoPagamento;

    @Column(name = "data_confirmacao_pagamento")
    private LocalDateTime dataConfirmacaoPagamento;

    @Column(name = "transacao_id_inter")
    private String transacaoIdInter;

    @Column(name = "status_inter")
    private String statusInter;

    @Column(name = "boleto_inter_compensado_como_pix")
    private Boolean boletoInterCompensadoComoPix = null;

    @OneToOne(mappedBy = "meioPagamento")
    @JsonBackReference
    private Compra compra;

    @Column(name = "id_pagarme")
    private String idPagarme;

    @Column(name = "codigo_pagarme")
    private String codigoPagarme;

    @Column(name = "status_pagarme")
    private String statusPagarme;

    @Column(name = "quantidade_parcelas_cartao")
    private Integer quantidadeParcelasCartao;

    @Column(name = "texto_pagamento", columnDefinition = "TEXT")
    private String textoPagamento;

    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    @Transactional
    public MeioPagamento atualizarMeioPagamento(PagarmeResponseDTO pagarmeResponse){

        if (pagarmeResponse == null || pagarmeResponse.getCharges() == null || pagarmeResponse.getCharges().isEmpty()) {
            return this;
        }

        ChargeDTO firstCharge = pagarmeResponse.getCharges().getFirst();

        setCodigoPagarme(firstCharge.getCode());
        setIdPagarme(firstCharge.getId());
        setStatusPagarme(pagarmeResponse.getStatus());

        if (firstCharge.getLastTransaction() != null && "credit_card".equals(firstCharge.getLastTransaction().getTransactionType())) {
            setQuantidadeParcelasCartao(firstCharge.getLastTransaction().getInstallments());
        }

        return this;
    }

    @Transactional
    public MeioPagamento atualizarMeioPagamento(WebhookBancoInterDTO.BancoInterResponseDTO bancoInterResponse){

        if (bancoInterResponse == null || bancoInterResponse.getCharges() == null || bancoInterResponse.getCharges().isEmpty()) {
            return this;
        }

        ChargeDTO firstCharge = bancoInterResponse.getCharges().getFirst();

        setCodigoPagarme(firstCharge.getCode());
        setIdPagarme(firstCharge.getId());
        setStatusInter(bancoInterResponse.getStatus());

        if (firstCharge.getLastTransaction() != null && "credit_card".equals(firstCharge.getLastTransaction().getTransactionType())) {
            setQuantidadeParcelasCartao(firstCharge.getLastTransaction().getInstallments());
        }

        return this;
    }

}
