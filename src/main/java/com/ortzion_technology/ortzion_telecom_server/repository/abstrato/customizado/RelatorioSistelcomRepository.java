package com.ortzion_technology.ortzion_telecom_server.repository.abstrato.customizado;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.RelatorioSistelcomRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.RelatorioAnaliticoDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.RelatorioSinteticoDTO;

import java.util.List;

public interface RelatorioSistelcomRepository {

    List<RelatorioSinteticoDTO> buscarDadosSinteticos(RelatorioSistelcomRequest request);

    List<RelatorioAnaliticoDTO> buscarDadosAnaliticos(RelatorioSistelcomRequest request);

}
