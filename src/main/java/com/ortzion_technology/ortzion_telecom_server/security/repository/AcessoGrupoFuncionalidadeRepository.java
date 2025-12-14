package com.ortzion_technology.ortzion_telecom_server.security.repository;

import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupoFuncionalidade;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupoFuncionalidadeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcessoGrupoFuncionalidadeRepository extends JpaRepository<AcessoGrupoFuncionalidade, AcessoGrupoFuncionalidadeId> {

    
}
