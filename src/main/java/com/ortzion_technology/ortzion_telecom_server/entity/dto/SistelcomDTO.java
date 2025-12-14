package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import com.ortzion_technology.ortzion_telecom_server.entity.vo.EmailVO;
import com.ortzion_technology.ortzion_telecom_server.entity.vo.SMSVO;
import com.ortzion_technology.ortzion_telecom_server.entity.vo.WhatsappVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SistelcomDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long validos = 0L;
    private Long invalidos = 0L;
    private Long higienizados = 0L;
    private Long total = 0L;
    private Long quantidadeSMS = 0L;

    private byte[] relatoriosExcelInvalido;

    private List<SMSVO.Destinatario> smsDestinatarios;
    private List<SMSVO.Removidos> smsRemovidos;

    private List<EmailVO.Destinatario> emailDestinatarios;
    private List<EmailVO.Removidos> emailRemovidos;

    private List<WhatsappVO.Destinatario> whatsappDestinatarios;
    private List<WhatsappVO.Removidos> whatsappRemovidos;

}
