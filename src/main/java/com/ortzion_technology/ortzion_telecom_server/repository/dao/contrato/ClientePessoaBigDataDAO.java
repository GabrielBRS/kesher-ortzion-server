package com.ortzion_technology.ortzion_telecom_server.repository.dao.contrato;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.AtualizarClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.FiltroClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.bigdata.pessoa.ClientePessoaBigData;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.FiltroClientePessoaBigDataDTO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientePessoaBigDataDAO {

    List<ClientePessoaBigData> pegarClientePessoaBigData(FiltroClientePessoaBigDataRequest filtroClientePessoaBigData);

    List<ClientePessoaBigData> buscarPorFiltroDinamico(FiltroClientePessoaBigDataDTO filtro);


}
