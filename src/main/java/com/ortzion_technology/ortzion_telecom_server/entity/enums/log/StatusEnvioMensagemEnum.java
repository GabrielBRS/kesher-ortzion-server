package com.ortzion_technology.ortzion_telecom_server.entity.enums.log;

public enum StatusEnvioMensagemEnum {

    ERRO(0, "Erro ao enviar pelo usuário ou sistema."),
    ENTREGUE(1, "Envio efetuado e confirmado."),
    PROCESSANDO(2, "Envio em processamento."),
    INVALIDO(3, "Destinatário inválido."),
    HIGIENIZADO(4, "Mensagem de conteúdo impróprio."),
    CANCELADO(5, "Envio cancelado pelo usuário ou sistema."),
    FALHOU(6, "Envio falhou."),
    ENVIANDO(7, "Mensagem está sendo enviada"),;

    private final int codigoNumerico;
    private final String descricao;

    StatusEnvioMensagemEnum(int codigoNumerico, String descricao) {
        this.codigoNumerico = codigoNumerico;
        this.descricao = descricao;
    }

    public int getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusEnvioMensagemEnum getByCodigoNumerico(int codigo) {
        for (StatusEnvioMensagemEnum status : values()) {
            if (status.codigoNumerico == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de pagamento inválido: " + codigo);
    }

}
