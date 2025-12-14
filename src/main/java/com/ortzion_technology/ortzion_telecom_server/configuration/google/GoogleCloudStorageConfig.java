//package com.ortzion_technology.ortzion_telecom_server.configuration.google;
//
//import com.google.auth.oauth2.ServiceAccountCredentials;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Configuration
//public class GoogleCloudStorageConfig {
//
//    private final GoogleCloudProperties properties;
//
//    public GoogleCloudStorageConfig(GoogleCloudProperties properties) {
//        this.properties = properties;
//    }
//
//    @Bean
//    public Storage googleStorage() throws IOException {
//        return StorageOptions.newBuilder()
//                .setProjectId(properties.getProjectId())
//                .setCredentials(
//                        ServiceAccountCredentials.fromStream(
//                                new FileInputStream(properties.getCredentialsPath())
//                        )
//                ).build().getService();
//    }
//}
