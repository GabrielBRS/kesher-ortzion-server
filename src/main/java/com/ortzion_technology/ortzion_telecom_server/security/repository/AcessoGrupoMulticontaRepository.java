package com.ortzion_technology.ortzion_telecom_server.security.repository;

import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupoMulticonta;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupoMulticontaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcessoGrupoMulticontaRepository extends JpaRepository<AcessoGrupoMulticonta, AcessoGrupoMulticontaId> {


}
