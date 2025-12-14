package com.ortzion_technology.ortzion_telecom_server.entity.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMSVO {

	private List<Destinatario> destinatarios = new ArrayList<>();
	private List<Removidos> higienizadas = new ArrayList<>();

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Destinatario {

		private String numero;
		private String mensagem;
		private String nomeCompleto;

		private String quantidadeCaratecteresMensagem;
		private String quantidadeSMS;

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Removidos {

        //1 invalido    2 higienizado
        private Integer motivo;
		private String numero;
		private String mensagem;
		private String nomeCompleto;

	}


}
