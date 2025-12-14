package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal;

import com.ortzion_technology.ortzion_telecom_server.entity.vo.EmailVO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailProcessadoResponse {

    private List<EmailVO.Destinatario> listaEmailEnviado;
    private List<EmailVO.Destinatario> listaEmailNaoEnviado;

}
