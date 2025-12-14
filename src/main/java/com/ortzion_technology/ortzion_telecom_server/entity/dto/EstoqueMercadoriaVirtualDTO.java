package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.*;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueMercadoriaVirtualDTO {

    private MulticontaRequestDTO multicontaRequestDTO;

    private Integer idMercadoriaVirtual;
    private Integer idCanalMensageria;
    private Long quantidadeParaEstoque;

    public EstoqueMercadoriaVirtualDTO(EstoqueMercadoriaVirtualRequest estoqueMercadoriaVirtualRequest) {
        this.multicontaRequestDTO = estoqueMercadoriaVirtualRequest.getMulticonta();
        this.idMercadoriaVirtual = estoqueMercadoriaVirtualRequest.getIdMercadoriaVirtual();
        this.quantidadeParaEstoque = estoqueMercadoriaVirtualRequest.getQuantidadeParaEstoque();
    }

    public EstoqueMercadoriaVirtualDTO(PedidoDisparoCanalMensageriaSMSRequest pedidoDisparoCanalMensageriaSMSRequest) {

        this.multicontaRequestDTO = new MulticontaRequestDTO();

        Long idUsuario = Optional.ofNullable(pedidoDisparoCanalMensageriaSMSRequest)
                .map(PedidoDisparoCanalMensageriaSMSRequest::getMulticonta)
                .map(MulticontaRequestDTO::getIdUsuario)
                .orElse(null);
        this.multicontaRequestDTO.setIdUsuario(idUsuario);

        Integer tipoSubjectus = Optional.ofNullable(pedidoDisparoCanalMensageriaSMSRequest)
                .map(PedidoDisparoCanalMensageriaSMSRequest::getMulticonta)
                .map(MulticontaRequestDTO::getTipoPessoa)
                .orElse(null);
        this.multicontaRequestDTO.setTipoPessoa(tipoSubjectus);


        Long idSubjectus = Optional.ofNullable(pedidoDisparoCanalMensageriaSMSRequest)
                .map(PedidoDisparoCanalMensageriaSMSRequest::getMulticonta)
                .map(MulticontaRequestDTO::getIdSubjectus)
                .orElse(null);
        this.multicontaRequestDTO.setIdSubjectus(idSubjectus);

        Integer idDepartamento = Optional.ofNullable(pedidoDisparoCanalMensageriaSMSRequest)
                .map(PedidoDisparoCanalMensageriaSMSRequest::getMulticonta)
                .map(MulticontaRequestDTO::getIdDepartamento)
                .orElse(null);
        this.multicontaRequestDTO.setIdDepartamento(idDepartamento);


        idCanalMensageria = Optional.ofNullable(pedidoDisparoCanalMensageriaSMSRequest)
                .stream()
                .map(PedidoDisparoCanalMensageriaSMSRequest::getIdCanalMensageria)
                .findFirst()
                .orElse(null);

    }

}
