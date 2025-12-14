package com.ortzion_technology.ortzion_telecom_server.security.config.mfa;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MfaConfig {

    @Bean
    public GoogleAuthenticator googleAuthenticator() {
        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder configBuilder =
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();

        configBuilder.setWindowSize(3);

        return new GoogleAuthenticator(configBuilder.build());
    }

}
