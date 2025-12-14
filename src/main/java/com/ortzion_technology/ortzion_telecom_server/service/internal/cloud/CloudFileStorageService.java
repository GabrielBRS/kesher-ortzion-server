package com.ortzion_technology.ortzion_telecom_server.service.internal.cloud;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.registro_acervo_documentos.RegistroAcervoPreCadastro;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.PreCadastroRepository;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.registro_acervo_documentos.RegistroAcervoPreCadastroRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import com.ortzion_technology.ortzion_telecom_server.service.external.integration.amazon.S3Service;
//import com.ortzion_technology.ortzion_telecom_server.service.external.integration.google.GoogleStorageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class CloudFileStorageService {

    private final Path fileStorageLocation = Paths.get("/tmp/uploads").toAbsolutePath().normalize();
//    private final GoogleStorageService googleStorageService;
    private final S3Service s3Service;
    private final RegistroAcervoPreCadastroRepository registroAcervoPreCadastroRepository;
    private final AcessoUsuarioRepository acessoUsuarioRepository;
    private final PreCadastroRepository preCadastroRepository;

    public CloudFileStorageService(S3Service s3Service,
                                   RegistroAcervoPreCadastroRepository registroAcervoPreCadastroRepository,
                                   AcessoUsuarioRepository acessoUsuarioRepository,
                                   PreCadastroRepository preCadastroRepository
    ) {
//        this.googleStorageService = googleStorageService;
        this.s3Service = s3Service;
        this.registroAcervoPreCadastroRepository = registroAcervoPreCadastroRepository;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.preCadastroRepository = preCadastroRepository;
//        this.s3Client = s3Client;

//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (IOException ex) {
//            throw new RuntimeException("Não foi possível criar o diretório de upload.", ex);
//        }
    }

    public String storeFile(MultipartFile file, Long userId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains(".")) ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String newFileName = userId + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10) + extension;

            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }

            Path targetLocation = fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            RegistroAcervoPreCadastro metadata = new RegistroAcervoPreCadastro();
            PreCadastro preCadastro = preCadastroRepository.getById(userId);
            metadata.setPreCadastro(preCadastro);
            metadata.setFileName(newFileName);
            metadata.setFilePath(targetLocation.toString());
            registroAcervoPreCadastroRepository.save(metadata);

            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao armazenar arquivo: " + file.getOriginalFilename(), ex);
        }
    }

    @Async("taskExecutorSMS")
    public void storeFilePreCadastro(
            MultipartFile file,
            MultipartFile fileRepresentanteLegal,
            MultipartFile fileProcuracaoRepresentanteLegal,
            MultipartFile fileContratoSocialRepresentanteLegal,
            PreCadastro preCadastro) {

        RegistroAcervoPreCadastro registroAcervoPreCadastro = this.registroAcervoPreCadastroRepository.pegarRegistroPreCadastroPorEmail(preCadastro.getEmail());

        if(registroAcervoPreCadastro != null) {
            throw new RuntimeException("Erro ao cadastrar registro");
        }

        try {
            salvarArquivo(file, preCadastro, "documento");
            salvarArquivo(fileRepresentanteLegal, preCadastro, "representante-legal");
            salvarArquivo(fileProcuracaoRepresentanteLegal, preCadastro, "procuracao-representante-legal");
            salvarArquivo(fileContratoSocialRepresentanteLegal, preCadastro, "contrato-social-representante-legal");

        } catch (IOException ex) {
            throw new RuntimeException("Erro ao armazenar arquivos no S3", ex);
        }

    }

    private void salvarArquivo(MultipartFile file, PreCadastro preCadastro, String tipoDocumento) throws IOException {
        if (file == null || file.isEmpty()){

        }else{
            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains(".")) ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String newFileName = preCadastro.getIdPreCadastro() + "-" + tipoDocumento + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10) + extension;
            String s3Key = "pre-cadastro/" + newFileName;

            // Upload para o S3 usando o GoogleCloud configurado
            s3Service.uploadFile(file,s3Key);
//        googleStorageService.uploadFile(file, s3Key);

            RegistroAcervoPreCadastro metadata = new RegistroAcervoPreCadastro();
            metadata.setPreCadastro(preCadastro);
            metadata.setFileName(newFileName);
            metadata.setFilePath(s3Key);
            metadata.setPreCadastro(preCadastro);
            registroAcervoPreCadastroRepository.save(metadata);
        }

    }
}
