package com.ortzion_technology.ortzion_telecom_server.security.repository;

import com.ortzion_technology.ortzion_telecom_server.security.entity.IpCredenciadoApiPagarmeSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IpCredenciadoApiPagarmeSecurityRepository extends JpaRepository<IpCredenciadoApiPagarmeSecurity, Long> {

    @Query("SELECT i.ipCredenciadoApiPagarme FROM IpCredenciadoApiPagarmeSecurity i")
    List<String> findAllIpAddresses();

    Optional<IpCredenciadoApiPagarmeSecurity> findByIpCredenciadoApiPagarme(String ip);

}
