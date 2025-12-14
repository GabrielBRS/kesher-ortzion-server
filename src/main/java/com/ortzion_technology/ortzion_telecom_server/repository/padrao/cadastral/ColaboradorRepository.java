package com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Colaborador;
import com.ortzion_technology.ortzion_telecom_server.repository.abstrato.customizado.ColaboradorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long>, ColaboradorRepositoryCustom {

    @Query(
            "select c from Colaborador c " +
                    "WHERE c.pessoa.documento = :documento"
    )
    Optional<Colaborador> pegarColaboradorPorDocumento(@Param("documento") String documento);

    /**
     * CORREÇÃO:
     * Renomeado de 'findAllByPessoa_Documento' para 'findAllByPessoa_DocumentoIn'.
     * A palavra-chave 'In' informa ao Spring Data JPA para gerar uma query 'WHERE ... IN (...)'
     * quando ele recebe uma Lista, o que corrige o erro "requires a scalar argument".
     */
    List<Colaborador> findAllByPessoa_DocumentoIn(List<String> documentos);

    // Este método está correto e é usado pelo 'pegarTodosColaboradores'
    List<Colaborador> findAllByEmpresa_IdEmpresa(Long idEmpresa);

}

