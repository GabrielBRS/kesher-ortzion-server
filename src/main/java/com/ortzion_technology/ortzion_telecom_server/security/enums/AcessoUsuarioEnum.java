package com.ortzion_technology.ortzion_telecom_server.security.enums;

public enum AcessoUsuarioEnum {

    INATIVO(0, "Status da conta inativo."),
    ATIVO(1, "Status da conta ativo."),
    PROCESSANDO(2, "Status da conta processando."),
    PRE_CADRASTO_CONFIRMADO(3, "Status da conta de pre-cadastro confirmado."),;

    private final int codigoNumerico;
    private final String descricao;

    AcessoUsuarioEnum(int codigoNumerico, String descricao) {
        this.codigoNumerico = codigoNumerico;
        this.descricao = descricao;
    }

    public int getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getDescricao() {
        return descricao;
    }

    public static AcessoUsuarioEnum getByCodigoNumerico(int codigo) {
        for (AcessoUsuarioEnum status : values()) {
            if (status.codigoNumerico == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de pagamento inválido: " + codigo);
    }

}
