package com.ortzion_technology.ortzion_telecom_server.entity.domain.suporte;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SuporteWebsocketMensagem implements Serializable {
    private String conteudo;
    private String remetente;
    private String hora;

}
