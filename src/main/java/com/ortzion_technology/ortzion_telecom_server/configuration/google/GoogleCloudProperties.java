//package com.ortzion_technology.ortzion_telecom_server.configuration.google;
//
//import jakarta.annotation.PostConstruct;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConfigurationProperties(prefix = "google.cloud.storage")
//@Getter
//@Setter
//public class GoogleCloudProperties {
//
//    @Value("${ambiente:dev}")
//    private String ambiente;
//
//    private String projectId;
//    private String bucketName;
//    private String credentialsPath;
//
//    @PostConstruct
//    public void init() {
//        configurarUrls();
//    }
//
//    private void configurarUrls() {
//        if ("dev".equalsIgnoreCase(this.ambiente)) {
//            this.credentialsPath = "/home/gabrielsousa/ortzion/google_cloud/credenciais.json";
//        } else {
//            this.credentialsPath = "/etc/ortzion/google_cloud/credenciais.json";
//        }
//    }
//}
//
