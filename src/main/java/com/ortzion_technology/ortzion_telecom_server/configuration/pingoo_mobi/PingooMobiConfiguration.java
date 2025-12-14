package com.ortzion_technology.ortzion_telecom_server.configuration.pingoo_mobi;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("pingoo.mobi")
public record PingooMobiConfiguration(String token, String user, String apiUrlSms, String apiUrlEmail) {};
