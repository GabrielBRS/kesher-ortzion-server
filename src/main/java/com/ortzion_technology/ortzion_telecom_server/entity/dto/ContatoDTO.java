package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ContatoDTO {

    private String countryCode;

    private String areaCode;

    private String number;

}
