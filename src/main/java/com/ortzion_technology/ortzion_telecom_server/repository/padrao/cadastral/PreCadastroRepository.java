package com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreCadastroRepository extends JpaRepository<PreCadastro, Long> {

    @Query(
            "select pc from PreCadastro pc where pc.email = :email"
    )
    PreCadastro pegarPreCadastroPorEmail(String email);

    PreCadastro getPreCadastroByDocumento(String documento);

    @Query(
            "select pc from PreCadastro pc where pc.documento = :documento"
    )
    PreCadastro pegarPreCadastroPorDocumento(@Param("documento") String documento);

    PreCadastro getPreCadastroByTipoPreCadastroAndIdPreCadastroOrDocumento(Integer tipoPreCadastro, Long idPreCadastro, String documento);

    List<PreCadastro> getPreCadastroByTipoPreCadastro(Integer tipo);

    @Query(
            "select pc from PreCadastro pc where pc.documentoEmpresa = :documento"
    )
    PreCadastro pegarPreCadastroPorCnpj(String documento);

}
