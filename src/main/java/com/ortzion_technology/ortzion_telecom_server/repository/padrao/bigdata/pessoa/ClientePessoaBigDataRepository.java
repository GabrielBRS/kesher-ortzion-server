package com.ortzion_technology.ortzion_telecom_server.repository.padrao.bigdata.pessoa;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.bigdata.pessoa.ClientePessoaBigData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ClientePessoaBigDataRepository extends JpaRepository<ClientePessoaBigData, Long> {

    @Query("""
            select cpdb from ClientePessoaBigData cpdb
            where cpdb.telefoneCompletoCliente in (:telefones)
               or cpdb.emailCliente in (:emails)
               or cpdb.documentoCliente in (:documentos)
            """)
    List<ClientePessoaBigData> buscarExistentesPorIdentificadores(
            @Param("telefones") Set<String> telefones,
            @Param("emails") Set<String> emails,
            @Param("documentos") Set<String> documentos
    );


}
