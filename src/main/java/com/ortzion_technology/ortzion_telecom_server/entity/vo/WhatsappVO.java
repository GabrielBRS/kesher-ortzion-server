package com.ortzion_technology.ortzion_telecom_server.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WhatsappVO {

	private List<Destinatario> destinatarios = new ArrayList<>();
	private List<Removidos> higienizadas = new ArrayList<>();

	private Long validas;
	private Long invalidas;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Destinatario {

		private String numero;
		private String mensagem;
		private String nomeCompleto;

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
