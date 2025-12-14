package com.ortzion_technology.ortzion_telecom_server.security.repository.custom;

import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MulticontaRepositoryCustom {

    List<MulticontaVO> pegarTodasMulticontasPorIdUsuario(Long idAcessoUsuario);

    Optional<Multiconta> pegarMulticontaPerfilEmpresarialColaborador(
            Long idUsuario,
            Integer tipoPessoa,
            Long idEmpresa,
            Integer idDepartamento
    );


}
