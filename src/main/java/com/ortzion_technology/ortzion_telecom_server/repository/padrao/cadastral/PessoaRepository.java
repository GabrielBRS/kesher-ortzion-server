package com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Query("SELECT p FROM Pessoa p WHERE p.email = :email")
    Pessoa findByEmail(@Param("email") String email);

    List<Pessoa> findAllByEmail(String email);

    @Query("SELECT p FROM Pessoa p WHERE p.documento = :documento")
    Pessoa pegarPessoaPorDocumento(@Param("documento") String documento);

    @Query("SELECT p FROM Pessoa p WHERE p.documento = :documento")
    Optional<Pessoa> pegarPessoaPorDocumentoOptional(@Param("documento") String documento);

    @Query("SELECT p FROM Pessoa p WHERE p.documento = :documento")
    List<Pessoa> pegarTodasPessoaPorDocumento(@Param("documento") String documento);

    Optional<Pessoa> findByDocumento(String documento);
}
