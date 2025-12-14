package com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.dto.ContatoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "contato", schema = "cadastral")
@SequenceGenerator(
        name = "seq_id_contato_generator",
        sequenceName = "cadastral.seq_id_contato",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContato;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "number")
    private String number;

    public Contato(ContatoDTO contatoDTO) {
        this.countryCode = contatoDTO.getCountryCode() != null ? contatoDTO.getCountryCode() : null;
        this.areaCode = contatoDTO.getAreaCode() != null ? contatoDTO.getAreaCode() : null;
        this.number = contatoDTO.getNumber() != null ? contatoDTO.getNumber() : null;
    }

}
