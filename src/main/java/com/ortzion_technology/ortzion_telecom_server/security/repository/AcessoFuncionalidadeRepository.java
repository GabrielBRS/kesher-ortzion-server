package com.ortzion_technology.ortzion_telecom_server.security.repository;

import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoFuncionalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcessoFuncionalidadeRepository extends JpaRepository<AcessoFuncionalidade, Long> {


}
