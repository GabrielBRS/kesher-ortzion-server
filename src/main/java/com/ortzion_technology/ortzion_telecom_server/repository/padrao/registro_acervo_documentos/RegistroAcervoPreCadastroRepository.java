package com.ortzion_technology.ortzion_telecom_server.repository.padrao.registro_acervo_documentos;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.registro_acervo_documentos.RegistroAcervoPreCadastro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RegistroAcervoPreCadastroRepository extends JpaRepository<RegistroAcervoPreCadastro, Long> {

    @Query("SELECT f FROM RegistroAcervoPreCadastro f WHERE f.preCadastro.email = :email")
    RegistroAcervoPreCadastro pegarRegistroPreCadastroPorEmail(String email);

}
