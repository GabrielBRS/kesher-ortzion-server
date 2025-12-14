package com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Endereco findByPessoa_Email(String email);

    Endereco findByEmpresa_Email(String email);

    @Query(
            "select e from Endereco e " +
                    "INNER JOIN Pessoa p on p.endereco.idEndereco = e.idEndereco " +
                    "WHERE p.documento = :documento"
    )
    Endereco pegarEnderecoPorPessoa(@Param("documento") String documento);

}
