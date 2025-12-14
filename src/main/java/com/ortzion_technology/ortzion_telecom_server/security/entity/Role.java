package com.ortzion_technology.ortzion_telecom_server.security.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles", schema = "security")
@SequenceGenerator(
        name = "seq_id_role_generator",
        sequenceName = "security.seq_id_role",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_id_role_generator")
    @Column(name = "id_role", unique = true, nullable = false)
    private Long idRole;

    @Column(unique = true, nullable = false)
    private String nome;

}