package com.ortzion_technology.ortzion_telecom_server.entity.enums.mercado;

public enum MercadoVirtualEnum {

    ORTZION_TELECOM(1L),
    OUTRO_MERCADO(2L);

    private final Long id;

    MercadoVirtualEnum(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

