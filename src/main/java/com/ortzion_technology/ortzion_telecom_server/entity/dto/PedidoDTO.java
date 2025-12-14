package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {

    Compra compra;

}
