package com.ortzion_technology.ortzion_telecom_server.entity.enums.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusPagamentoEnum;

public enum RolesEnum {

    ROLE_ADMIN(1L, "Adiciona a role admin no sistema."),
    ROLE_USER(2L, "Adiciona a role user no sistema.");

    private final Long codigoNumerico;
    private final String descricao;

    RolesEnum(Long codigoNumerico, String descricao) {
        this.codigoNumerico = codigoNumerico;
        this.descricao = descricao;
    }

    public Long getCodigoNumerico() {
        return codigoNumerico;
    }

    public String getDescricao() {
        return descricao;
    }

    public static RolesEnum getByCodigoNumerico(Long codigo) {
        for (RolesEnum status : values()) {
            if (status.codigoNumerico == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de pagamento inválido: " + codigo);
    }

}
