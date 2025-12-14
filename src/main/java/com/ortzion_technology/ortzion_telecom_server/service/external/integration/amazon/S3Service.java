package com.ortzion_technology.ortzion_telecom_server.service.external.integration.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${s3.bucket.name}") // Defina este valor no application.properties
    private String bucketName;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file, String s3Key) {
        try (InputStream is = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // ** CORREÇÃO APLICADA AQUI: Usando s3Key no putObject **
            s3Client.putObject(bucketName, s3Key, is, metadata);

            // ** CORREÇÃO APLICADA AQUI: Usando s3Key no getUrl para obter a URL correta **
            return s3Client.getUrl(bucketName, s3Key).toString(); // Retorna a URL pública do objeto
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo para o S3: " + s3Key, e);
        }
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer download do arquivo do S3", e);
        }
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }
}
