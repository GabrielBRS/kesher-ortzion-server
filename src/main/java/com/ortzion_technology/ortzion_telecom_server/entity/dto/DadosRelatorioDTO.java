package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DadosRelatorioDTO {

    private final SistelcomDTO sistelcomDTO;
    private final EstoqueMercadoriaVirtual estoqueMercadoriaVirtual;
    private final CampanhaMensageria campanhaMensageria;
    private final Integer idCanalMensageria;

}
