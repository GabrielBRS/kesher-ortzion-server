package com.ortzion_technology.ortzion_telecom_server.entity.domain.registro_acervo_documentos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_acervo_pre_cadastro", schema = "registro_acervo_documentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroAcervoPreCadastro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_registro_acervo_pre_cadastro", nullable = false)
    private Long idRegistroAcervoPreCadastro;

    @Column(name = "file_name", nullable = false, unique = true, length = 255)
    private String fileName;

    @Column(name = "file_path", nullable = false, columnDefinition = "TEXT")
    private String filePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pre_cadastro", referencedColumnName = "id_pre_cadastro", nullable = false)
    private PreCadastro preCadastro;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
