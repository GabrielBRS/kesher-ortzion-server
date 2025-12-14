package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.relatorio;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.RelatorioSistelcomRequest;
import com.ortzion_technology.ortzion_telecom_server.repository.abstrato.customizado.RelatorioSistelcomRepository;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.RelatorioAnaliticoDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.RelatorioSinteticoDTO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.exception.RelatorioGenerationException;
import com.ortzion_technology.ortzion_telecom_server.service.internal.exception.RelatorioVazioException;
import com.ortzion_technology.ortzion_telecom_server.service.internal.relatorio.GeradorRelatorioAnaliticoService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.relatorio.GeradorRelatorioSinteticoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class RelatorioSistelcomService {

    private final RelatorioSistelcomRepository relatorioSistelcomRepository;
    private final GeradorRelatorioAnaliticoService geradorRelatorioAnaliticoService;
    private final GeradorRelatorioSinteticoService geradorRelatorioSinteticoService;

    public static class RelatorioGerado {
        private final byte[] dados;
        private final String nomeArquivo;
        private final String mediaType;

        public RelatorioGerado(byte[] dados, String nomeArquivo, String mediaType) {
            this.dados = dados;
            this.nomeArquivo = nomeArquivo;
            this.mediaType = mediaType;
        }

        public byte[] getDados() { return dados; }
        public String getNomeArquivo() { return nomeArquivo; }
        public String getMediaType() { return mediaType; }
    }

    public RelatorioSistelcomService(RelatorioSistelcomRepository relatorioSistelcomRepository,
                                     GeradorRelatorioAnaliticoService geradorRelatorioAnaliticoService,
                                     GeradorRelatorioSinteticoService geradorRelatorioSinteticoService) {
        this.relatorioSistelcomRepository = relatorioSistelcomRepository;
        this.geradorRelatorioAnaliticoService = geradorRelatorioAnaliticoService;
        this.geradorRelatorioSinteticoService = geradorRelatorioSinteticoService;
    }

    @Transactional(readOnly = true)
    public RelatorioGerado gerarRelatorio(RelatorioSistelcomRequest relatorioSistelcomRequest) {

        if (Objects.equals(relatorioSistelcomRequest.getTipoRelatorio(), 1)) {
            List<RelatorioAnaliticoDTO> listaDeDados = this.relatorioSistelcomRepository.buscarDadosAnaliticos(relatorioSistelcomRequest);

            if (listaDeDados == null || listaDeDados.isEmpty()) {
                throw new RelatorioVazioException("Nenhum dado encontrado para gerar o relatório analítico com os filtros fornecidos.");
            }
            return criarRelatorioAnaliticoZipado(listaDeDados);
        } else {
            List<RelatorioSinteticoDTO> listaDeDados = this.relatorioSistelcomRepository.buscarDadosSinteticos(relatorioSistelcomRequest);

            if (listaDeDados == null || listaDeDados.isEmpty()) {
                throw new RelatorioVazioException("Nenhum dado encontrado para gerar o relatório sintético com os filtros fornecidos.");
            }
            return criarRelatorioSinteticoZipado(listaDeDados);
        }
    }

    private RelatorioGerado criarRelatorioAnaliticoZipado(List<RelatorioAnaliticoDTO> listaDeDados) {
        try {
            byte[] relatorioExcelBytes = this.geradorRelatorioAnaliticoService.gerarRelatorioAnaliticoExcel(listaDeDados);

            if (relatorioExcelBytes == null || relatorioExcelBytes.length == 0) {
                throw new RelatorioVazioException("Nenhum conteúdo Excel foi gerado para o relatório analítico.");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                ZipEntry zipEntry = new ZipEntry("relatorio_analitico_" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".xlsx");
                zos.putNextEntry(zipEntry);
                zos.write(relatorioExcelBytes);
                zos.closeEntry();
            }

            byte[] zipAsBytes = baos.toByteArray();
            if (zipAsBytes.length <= 22) {
                throw new RelatorioVazioException("Nenhum arquivo zip foi gerado para o relatório analítico.");
            }

            String zipFilename = "relatorio_analitico_" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".zip";
            return new RelatorioGerado(zipAsBytes, zipFilename, "application/zip");

        } catch (IOException e) {
            throw new RelatorioGenerationException("Erro ao compactar o relatório analítico: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RelatorioGenerationException("Erro ao gerar o relatório analítico: " + e.getMessage(), e);
        }
    }

    private RelatorioGerado criarRelatorioSinteticoZipado(List<RelatorioSinteticoDTO> listaDeDados) {
        try {

            byte[] relatorioExcelBytes = this.geradorRelatorioSinteticoService.gerarRelatorioSinteticoExcel(listaDeDados);

            if (relatorioExcelBytes == null || relatorioExcelBytes.length == 0) {
                throw new RelatorioVazioException("Nenhum conteúdo Excel foi gerado para o relatório sintético.");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                ZipEntry zipEntry = new ZipEntry("relatorio_sintetico_" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".xlsx");
                zos.putNextEntry(zipEntry);
                zos.write(relatorioExcelBytes);
                zos.closeEntry();
            }

            byte[] zipAsBytes = baos.toByteArray();
            if (zipAsBytes.length <= 22) {
                throw new RelatorioVazioException("Nenhum arquivo zip foi gerado para o relatório sintético.");
            }

            String zipFilename = "relatorio_sintetico_" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".zip";
            return new RelatorioGerado(zipAsBytes, zipFilename, "application/zip"); // Retorna o ZIP

        } catch (IOException e) {
            throw new RelatorioGenerationException("Erro ao compactar o relatório sintético: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RelatorioGenerationException("Erro ao gerar o relatório sintético: " + e.getMessage(), e);
        }
    }
}