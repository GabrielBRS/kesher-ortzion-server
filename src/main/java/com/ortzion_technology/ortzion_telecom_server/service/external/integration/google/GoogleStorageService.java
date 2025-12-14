//package com.ortzion_technology.ortzion_telecom_server.service.external.integration.google;
//
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
////import com.ortzion_technology.ortzion_telecom_server.configuration.google.GoogleCloudProperties;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//public class GoogleStorageService {
//
//    private final Storage storage;
//    private final GoogleCloudProperties properties;
//
//    public GoogleStorageService(Storage storage, GoogleCloudProperties properties) {
//        this.storage = storage;
//        this.properties = properties;
//    }
//
//    public void uploadFile(MultipartFile file, String fileName) throws IOException {
//        BlobInfo blobInfo = BlobInfo.newBuilder(properties.getBucketName(), fileName).build();
//        storage.create(blobInfo, file.getBytes());
//    }
//}
