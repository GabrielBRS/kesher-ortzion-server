package com.ortzion_technology.ortzion_telecom_server.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "ip_credenciado_api_pagarme", schema = "security")
@SequenceGenerator(
        name = "seq_id_ip_credenciado_api_pagarme_generator",
        sequenceName = "security.seq_id_ip_credenciado_api_pagarme",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class IpCredenciadoApiPagarmeSecurity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_id_ip_credenciado_api_pagarme_generator")
    @Column(name = "id_ip_credenciado_api_pagarme", unique = true, nullable = false)
    private Long idIpCredenciadoApiPagarme;

    @Column(name = "ip_credenciado_api_pagarme", unique = true, nullable = true)
    private String ipCredenciadoApiPagarme;

}
