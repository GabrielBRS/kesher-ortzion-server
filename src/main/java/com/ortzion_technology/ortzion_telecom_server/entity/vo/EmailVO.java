package com.ortzion_technology.ortzion_telecom_server.entity.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class EmailVO {
	
	private List<Destinatario> destinatarios = new ArrayList<>();
	private List<Removidos> higienizadas = new ArrayList<>();

	private Long validas;
	private Long invalidas;

	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@EqualsAndHashCode
	public static class Destinatario {

		private String emailRemetente;

		private String nomeRemetente;

		private String emailDestinatario;

		private String nomeDestinatario;

		private String tituloEmail;

		private String bodyEmail;

	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Removidos {

        //1 invalido    2 higienizado
        private Integer motivo;

		private String emailRemetente;

		private String nomeRemetente;

		private String emailDestinatario;

		private String nomeDestinatario;

		private String tituloEmail;

		private String bodyEmail;

	}

}
