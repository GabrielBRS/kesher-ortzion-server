package com.ortzion_technology.ortzion_telecom_server.entity.enums.log;

public enum StatusPrioridadeMensagemEnum {

    BAIXA(1, "Status prioridade baixa."),
    MEDIA(2, "Status prioridade media."),
    ALTA(3, "Status prioridade alta.");

    private final int codigoNumerico;
    private final String descricao;

    StatusPrioridadeMensagemEnum(int codigoNumerico, String descricao) {
        this.codigoNumerico = codigoNumerico;
        this.descricao = descricao;
    }

    public int getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusPrioridadeMensagemEnum getByCodigoNumerico(int codigo) {
        for (StatusPrioridadeMensagemEnum status : values()) {
            if (status.codigoNumerico == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de pagamento inválido: " + codigo);
    }

}
