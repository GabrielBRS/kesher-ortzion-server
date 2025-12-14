package com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.LGPD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LGPDRepository extends JpaRepository<LGPD, Long> {

}
