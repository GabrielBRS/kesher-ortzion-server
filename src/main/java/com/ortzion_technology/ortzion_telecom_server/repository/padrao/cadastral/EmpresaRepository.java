package com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query(" SELECT e FROM Empresa e JOIN FETCH Multiconta m ON m.multicontaId.idSubjectus = e.idEmpresa WHERE m.multicontaId.idSubjectus = :idEmpresa AND m.multicontaId.tipoPessoa = 2 ")
    List<Empresa> pegarEmpresaMulticonta(@Param("idEmpresa") Long idEmpresa);

    @Query("SELECT p FROM Empresa p WHERE p.cnpj = :cnpj")
    Empresa pegarEmpresaCnpj(@Param("cnpj") String cnpj);

    @Query(
            "SELECT e FROM Empresa e " +
                    "WHERE 1 = 1 " +
                    "AND e.idEmpresa = :idEmpresa "
    )
    Empresa pegarEmpresaPorIdEmpresa(@Param("idEmpresa") Long idEmpresa);



}
