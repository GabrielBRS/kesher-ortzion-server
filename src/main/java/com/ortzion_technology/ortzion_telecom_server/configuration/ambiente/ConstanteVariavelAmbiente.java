package com.ortzion_technology.ortzion_telecom_server.configuration.ambiente;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class ConstanteVariavelAmbiente {

    @Value("${ambiente:dev}") // pega do application.properties, padrão "dev"
    private String ambiente;

    private String httpUrl;
    private String httpsAwsUrl;
    private String httpsOtimaDigitalSms;
    private String httpRedirectSite;
    private String httpLinkPreCadastro;
    private String httpLinkFront;
    private String httpLinkFrontend;
    private String remetenteEmailVerificadoSendGrid;
    private String remetenteEmailVerificadoBrevo;

    public ConstanteVariavelAmbiente(@Value("${ambiente:dev}") String ambiente) {
        this.ambiente = ambiente;
        configurarUrls();
    }

    private void configurarUrls() {
        if ("dev".equalsIgnoreCase(this.ambiente)) {
            this.httpUrl = "http://localhost:28080/api/v1/";
            this.httpsAwsUrl = "http://localhost:28080/api/v1/";
            this.httpsOtimaDigitalSms = "https://api360.otima.digital/send.php";
            this.httpRedirectSite = "http://localhost:4200/login";
            this.httpLinkPreCadastro = "http://localhost:28080/api/v1/pre-cadastro/email-confirmado?email=";
            this.httpLinkFront = "http://localhost:4200/login";
            this.httpLinkFrontend = "http://localhost:4200";
            this.remetenteEmailVerificadoSendGrid = "no-reply@ortzion.com.br";
            this.remetenteEmailVerificadoBrevo = "no-reply@ortzion.com.br";

        } else {
            this.httpUrl = "https://api.ortzion.com.br/api/v1/";
            this.httpsAwsUrl = "https://api.ortzion.com.br/api/v1/";
            this.httpsOtimaDigitalSms = "https://api360.otima.digital/send.php";
            this.httpRedirectSite = "https://kesher.ortzion.com.br/login";
            this.httpLinkPreCadastro = "https://api.ortzion.com.br/api/v1/pre-cadastro/email-confirmado?email=";
            this.httpLinkFront = "https://kesher.ortzion.com.br/login";
            this.httpLinkFrontend = "https://kesher.ortzion.com.br";
            this.remetenteEmailVerificadoSendGrid = "no-reply@ortzion.com.br";
            this.remetenteEmailVerificadoBrevo = "no-reply@ortzion.com.br";

        }

    }
}
