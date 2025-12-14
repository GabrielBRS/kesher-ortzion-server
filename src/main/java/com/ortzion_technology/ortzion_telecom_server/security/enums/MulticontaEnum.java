package com.ortzion_technology.ortzion_telecom_server.security.enums;

public enum MulticontaEnum {

    INATIVO(0, "Status da multiconta inativo."),
    ATIVO(1, "Status da multiconta ativo."),
    PROCESSANDO(2, "Status da multiconta processando.");

    private final int codigoNumerico;
    private final String descricao;

    MulticontaEnum(int codigoNumerico, String descricao) {
        this.codigoNumerico = codigoNumerico;
        this.descricao = descricao;
    }

    public int getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getDescricao() {
        return descricao;
    }

    public static MulticontaEnum getByCodigoNumerico(int codigo) {
        for (MulticontaEnum status : values()) {
            if (status.codigoNumerico == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de pagamento inválido: " + codigo);
    }

}
