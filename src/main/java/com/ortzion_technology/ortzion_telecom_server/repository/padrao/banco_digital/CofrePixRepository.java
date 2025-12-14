package com.ortzion_technology.ortzion_telecom_server.repository.padrao.banco_digital;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.banco_digital.CofrePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CofrePixRepository extends JpaRepository<CofrePix, Long> {

    @Query(
            "SELECT cp FROM CofrePix cp " +
                    "WHERE cp.statusChavePix = 1 " +
                    "ORDER BY cp.dataCadastroChavePix ASC "
    )
    CofrePix pegarChavePixAtiva();

}
