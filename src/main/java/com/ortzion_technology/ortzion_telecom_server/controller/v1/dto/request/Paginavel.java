package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Paginavel {

    int total;
    int pagina;
    String sortDirection;
    String sortBy;

}
