package com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GestaoEstoqueMercadoriaVirtualEmpresarialRepository extends JpaRepository<GestaoEstoqueMercadoriaVirtualEmpresarial, Long> {

    @Query(
            """
            select gemve from GestaoEstoqueMercadoriaVirtualEmpresarial gemve where gemve.empresa.idEmpresa = :idEmpresa
            """
    )
    List<GestaoEstoqueMercadoriaVirtualEmpresarial> buscarTodosPorIdEmpresa(Long idEmpresa);

}

