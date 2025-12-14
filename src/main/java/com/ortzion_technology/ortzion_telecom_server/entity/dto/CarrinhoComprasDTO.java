package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CarrinhoComprasRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoComprasDTO {

    List<PedidoDTO> pedidosDTOS = new ArrayList<>();

    public CarrinhoComprasDTO(CarrinhoComprasRequest carrinhoComprasRequest) {

    }

}
