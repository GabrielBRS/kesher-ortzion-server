package com.ortzion_technology.ortzion_telecom_server.entity.domain.suporte;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fale_conosco", schema = "suporte")
@SequenceGenerator(
        name = "seq_id_fale_conosco_generator",
        sequenceName = "suporte.seq_id_fale_conosco",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FaleConosco implements Serializable {

    @Id
    @Column(name = "id_fale_conosco")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_fale_conosco_generator")
    private Long idFaleConosco;

    @NotBlank(message = "O título não pode estar em branco.")
    @Size(max = 255, message = "O título não pode exceder 255 caracteres.")
    @Column(name = "titulo", length = 255)
    private String titulo;

    @NotBlank(message = "O telefone не pode estar em branco.")
    @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
    @Column(name = "telefone", length = 20)
    private String telefone;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "Formato de e-mail inválido.")
    @Size(max = 100, message = "O e-mail não pode exceder 100 caracteres.")
    @Column(name = "email", length = 100)
    private String email;

    @NotBlank(message = "A mensagem não pode estar em branco.")
    @Size(max = 1000, message = "A mensagem não pode exceder 1000 caracteres.")
    @Column(name = "mensagem", length = 1000, columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "data_solicitacao", nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    @Setter
    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @PrePersist
    protected void onCreate() {
        this.dataSolicitacao = LocalDateTime.now();
    }

}
