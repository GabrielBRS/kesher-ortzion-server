package com.ortzion_technology.ortzion_telecom_server.entity.enums.log;

public enum StatusPagamentoEnum {

    CANCELADO(0, "Pagamento cancelado pelo usuário ou sistema."),
    PAGO(1, "Pagamento efetuado e confirmado."),
    PROCESSANDO(2, "Pagamento em processamento aguardando confirmação."),
    FALHO(3, "Pagamento falho."),
    PENDENTE(4, "Pagamento pendente."),
    RECUSADO(5, "Pagamento recusado."),
    INSUFICENTE(6, "Pagamento insuficiente."),
    PAGO_A_MAIS(7, "Pagamento pago a mais."),
    ESTORNADO(9, "Pagamento estornado."),
    CONCLUIDO(10, "Pagamento concluido."),
    CANCELADO_FINALIZADO(11, "Pagamento cancelado finalizado.");

    private final int codigoNumerico;
    private final String descricao;

    StatusPagamentoEnum(int codigoNumerico, String descricao) {
        this.codigoNumerico = codigoNumerico;
        this.descricao = descricao;
    }

    public int getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusPagamentoEnum getByCodigoNumerico(int codigo) {
        for (StatusPagamentoEnum status : values()) {
            if (status.codigoNumerico == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de pagamento inválido: " + codigo);
    }

}
