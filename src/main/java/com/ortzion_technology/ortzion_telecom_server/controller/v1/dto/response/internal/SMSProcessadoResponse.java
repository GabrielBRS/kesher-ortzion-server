package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal;

import com.ortzion_technology.ortzion_telecom_server.entity.vo.SMSVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMSProcessadoResponse {

    private List<SMSVO.Destinatario> listaSMSEnviado;
    private List<SMSVO.Destinatario> listaSMSNaoEnviado;

}
