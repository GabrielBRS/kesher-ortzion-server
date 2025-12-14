package com.ortzion_technology.ortzion_telecom_server.security.io.email;

import brevo.ApiException;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailAttachment;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import com.ortzion_technology.ortzion_telecom_server.configuration.ambiente.ConstanteVariavelAmbiente;
import com.ortzion_technology.ortzion_telecom_server.configuration.pingoo_mobi.PingooMobiConfiguration;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.AtualizacaoPreCadastroDTO;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.EmpresaRepository;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.PessoaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.criptografia.TokenEncryptorDecryptorService;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EnvioEmailService {

    private final ConstanteVariavelAmbiente constante;

    private final TemplateEngine templateEngine;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestTemplate restTemplate;
    private final JSONObject configuracoes;
    private final PessoaRepository pessoaRepository;
    private final EmpresaRepository empresaRepository;
    private final TokenEncryptorDecryptorService tokenEncryptorDecryptorService;

    private final TransactionalEmailsApi brevoApi;

    public EnvioEmailService(ConstanteVariavelAmbiente constante,
                             TemplateEngine templateEngine,
                             BCryptPasswordEncoder bCryptPasswordEncoder,
                             RestTemplate restTemplate,
                             PingooMobiConfiguration pingooMobiConfiguration, PessoaRepository pessoaRepository, EmpresaRepository empresaRepository, TokenEncryptorDecryptorService tokenEncryptorDecryptorService,
                             TransactionalEmailsApi brevoApi) throws JSONException {
        this.constante = constante;
        this.templateEngine = templateEngine;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restTemplate = restTemplate;
        this.pessoaRepository = pessoaRepository;
        this.empresaRepository = empresaRepository;
        this.tokenEncryptorDecryptorService = tokenEncryptorDecryptorService;
        this.brevoApi = brevoApi;

        this.configuracoes = new JSONObject();
        this.configuracoes.put("token", pingooMobiConfiguration.token());
        this.configuracoes.put("user", pingooMobiConfiguration.user());
        this.configuracoes.put("url", pingooMobiConfiguration.apiUrlEmail());
    }

    private void enviarEmailViaBrevo(String toEmailAddress, String subject, String htmlContent, MultipartFile attachment) throws IOException {

        SendSmtpEmailSender sender = new SendSmtpEmailSender()
                .name("Kesher Ortzion")
                .email(constante.getRemetenteEmailVerificadoBrevo());

        SendSmtpEmailTo to = new SendSmtpEmailTo()
                .email(toEmailAddress);

        SendSmtpEmail email = new SendSmtpEmail()
                .sender(sender)
                .to(Collections.singletonList(to))
                .subject(subject)
                .htmlContent(htmlContent);

        if (attachment != null && !attachment.isEmpty()) {
            try {
                byte[] fileContent = attachment.getBytes();
                SendSmtpEmailAttachment brevoAttachment = new SendSmtpEmailAttachment()
                        .name(attachment.getOriginalFilename())
                        .content(fileContent);
                email.setAttachment(Collections.singletonList(brevoAttachment));
            } catch (IOException e) {
                System.err.println("Erro ao processar anexo para Brevo: " + e.getMessage());
                throw new IOException("Falha ao anexar arquivo ao email: " + e.getMessage(), e);
            }
        }

        try {
            brevoApi.sendTransacEmail(email);
            System.out.println("Email enviado com sucesso via Brevo API para: " + toEmailAddress);
        } catch (ApiException e) {
            System.err.println("Falha ao enviar e-mail via Brevo API. Status: " + e.getCode() + ", Body: " + e.getResponseBody());
            throw new IOException("Falha ao enviar e-mail via Brevo API: " + e.getResponseBody(), e);
        }

    }


    @Async("taskExecutorSMS")
    public void enviarEmailConfirmacaoCadastro(PreCadastro preCadastro) throws IOException {

        Context context = new Context();
        context.setVariable("nome", preCadastro.getNome());
        context.setVariable("linkPrecadastro", constante.getHttpLinkPreCadastro() + URLEncoder.encode(preCadastro.getEmail(), StandardCharsets.UTF_8));
        context.setVariable("linkFront", constante.getHttpLinkFront());

        String htmlContent;
        String subject = "Confirme seu pré-cadastro";

        if(preCadastro.getTipoPreCadastro() == 1){
            htmlContent = templateEngine.process("email-confirmacao", context);
        } else {
            context.setVariable("nomeRepresentanteLegal", preCadastro.getNome());
            context.setVariable("instituicao", preCadastro.getNome());
            context.setVariable("cnpj", preCadastro.getDocumento());
            htmlContent = templateEngine.process("email-confirmacao-pj", context);
        }

        enviarEmailViaBrevo(preCadastro.getEmail(), subject, htmlContent, null);
    }

    @Async("taskExecutorSMS")
    public void enviarEmailAtualizacaoCadastro(PreCadastro preCadastro, AtualizacaoPreCadastroDTO peticao, MultipartFile destinatarioFile) throws IOException {

        String htmlContent = "";
        String subject = "Atualização de Cadastro";

        if(preCadastro.getTipoPreCadastro() != 1){

             String senhaProvisoria = this.tokenEncryptorDecryptorService.gerarSenhaAleatoria();
             String senhaCriptografada = bCryptPasswordEncoder.encode(senhaProvisoria);

            if(preCadastro.getStatusPreCadastro() == 1){
                Context context = new Context();
                context.setVariable("nomeRepresentanteLegal", preCadastro.getNome());
                 context.setVariable("senhaProvisoria", senhaProvisoria);
                context.setVariable("linkPrecadastro", constante.getHttpLinkPreCadastro() + URLEncoder.encode(preCadastro.getEmail(), StandardCharsets.UTF_8));
                context.setVariable("linkFront", constante.getHttpLinkFront());
                context.setVariable("instituicao", preCadastro.getNome());
                context.setVariable("cnpj", preCadastro.getDocumento());
                context.setVariable("email", preCadastro.getEmail());
                htmlContent = templateEngine.process("primeiro-acesso-pj", context);
                subject = "Primeiro Acesso";
            }
            else if(preCadastro.getStatusPreCadastro() == 2){
                Context context = new Context();
                context.setVariable("nomeRepresentanteLegal", preCadastro.getNome());
                context.setVariable("instituicao", preCadastro.getNome());
                context.setVariable("cnpj", preCadastro.getDocumento());

                if (destinatarioFile != null && !destinatarioFile.isEmpty()) {
                    context.setVariable("nomeArquivo", destinatarioFile.getOriginalFilename());
                }
                htmlContent = templateEngine.process("email-correcao-pj", context);
                subject = "Correção de Pré-cadastro";
                enviarEmailViaBrevo(preCadastro.getEmail(), subject, htmlContent, destinatarioFile);
                return;
            }
            else if(preCadastro.getStatusPreCadastro() == 3){
                Context context = new Context();
                context.setVariable("nomeRepresentanteLegal", preCadastro.getNome());
                context.setVariable("instituicao", preCadastro.getNome());
                context.setVariable("cnpj", preCadastro.getDocumento());
                if (destinatarioFile != null && !destinatarioFile.isEmpty()) {
                    context.setVariable("pdf", destinatarioFile.getOriginalFilename());
                }
                htmlContent = templateEngine.process("email-negacao-pj", context);
                subject = "Pré-cadastro Negado";
            } else {
                System.err.println("Status de pré-cadastro desconhecido para email de atualização: " + preCadastro.getStatusPreCadastro());
                return;
            }
        } else {
            System.out.println("Nenhuma ação de email definida para atualização de cadastro de Pessoa Física neste método.");
            return;
        }
        enviarEmailViaBrevo(preCadastro.getEmail(), subject, htmlContent, null);
    }


    public void enviarEmailRedefinirSenha(Pessoa pessoa, AcessoUsuario acessoUsuario, String token) throws IOException {

        Context context = new Context();
        context.setVariable("nome", pessoa.getNomeCompleto());

        String urlBackend = constante.getHttpLinkFrontend() + "/nova-senha?token=" + token;

        context.setVariable("httpLinkRedefinirSenha", urlBackend);

        String htmlContent = templateEngine.process("email-solicitar-redefinir-senha", context);
        String subject = "Confirme seu pedido de redefinição de senha";

        enviarEmailViaBrevo(pessoa.getEmail(), subject, htmlContent, null);
    }

    public void enviarEmailPrimeiroLoginSenhaProvisoria(PreCadastro preCadastro, String senhaProvisoria) throws IOException {

        Context context = new Context();
        context.setVariable("nome", preCadastro.getNome());
        context.setVariable("senhaProvisoria", senhaProvisoria);
        context.setVariable("linkPrecadastro", constante.getHttpLinkPreCadastro() + URLEncoder.encode(preCadastro.getEmail(), StandardCharsets.UTF_8));
        context.setVariable("linkFront", constante.getHttpLinkFront());

        String htmlContent;
        String subject = "Primeiro Acesso";

        if(preCadastro.getTipoPreCadastro() == 1){
            htmlContent = templateEngine.process("primeiro-acesso", context);
        } else {
            context.setVariable("nomeRepresentanteLegal", preCadastro.getNome());
            context.setVariable("instituicao", preCadastro.getNome());
            context.setVariable("cnpj", preCadastro.getDocumento());
            htmlContent = templateEngine.process("primeiro-acesso-pj", context);
        }
        enviarEmailViaBrevo(preCadastro.getEmail(), subject, htmlContent, null);
    }

    @Async("taskExecutorEmailLogin")
    public void enviarTokenAcesso(AcessoUsuario acessoUsuario, String tokenProvisorio) throws IOException {

        Optional<Pessoa> pessoaOptional = pessoaRepository.findByDocumento(acessoUsuario.getDocumentoUsuario());

        if (pessoaOptional.isEmpty()) {
            throw new IllegalStateException("Não foi possível encontrar os dados cadastrais para o usuário com documento: " + acessoUsuario.getDocumentoUsuario());
        }

        Pessoa pessoa = pessoaOptional.get();

        Context context = new Context();
        context.setVariable("nome", pessoa.getNome());
        context.setVariable("tokenAcesso", tokenProvisorio);
        context.setVariable("linkFront", constante.getHttpLinkFront());

        String htmlContent = templateEngine.process("email-token-acesso", context);
        String subject = "Token Acesso - Ortzion Telecom";

        enviarEmailViaBrevo(acessoUsuario.getEmailUsuario(), subject, htmlContent, null);
    }

}