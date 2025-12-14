package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.sms;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.vo.SMSVO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.filtros.SmsFilterService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.DocumentoProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSmsDocumentProcessor implements DocumentoProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSmsDocumentProcessor.class);

    private static final String SMS_SIMPLES_REGEX = "^[A-Za-z0-9 \\r\\n@£$¥èéùìòÇØøÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ!\"#¤%&'()*+,-./:;<=>?¡ÄÖÑÜ§¿äöñüà^{}\\[~\\]|€]*$";
    private static final int GSM_SINGLE_SMS_LIMIT = 160;
    private static final int UNICODE_SINGLE_SMS_LIMIT = 70;
    private static final int GSM_CONCATENATED_SMS_LIMIT = 153;
    private static final int UNICODE_CONCATENATED_SMS_LIMIT = 67;
    private static final int MAX_SMS_PARTS = 6;

    private static final String MOTIVO_TELEFONE_INVALIDO = "Telefone inválido (deve conter 8 ou 9 dígitos). ";
    private static final String MOTIVO_DDD_INVALIDO = "DDD inválido (deve conter 2 dígitos). ";
    private static final String MOTIVO_MENSAGEM_VAZIA = "Mensagem não fornecida ou vazia. ";
    private static final String MOTIVO_CONTEUDO_BLOQUEADO = "Mensagem contém conteúdo bloqueado. ";
    private static final String MOTIVO_MENSAGEM_MUITO_LONGA = "Mensagem excede o limite máximo de %d caracteres. ";

    protected final SmsFilterService smsFilterService;

    protected AbstractSmsDocumentProcessor(SmsFilterService smsFilterService) {
        this.smsFilterService = smsFilterService;
    }

    protected SistelcomDTO processarLinhas(List<String[]> linhas, PedidoDisparoCanalMensageriaSMSRequest pedido) {
        List<SMSVO.Destinatario> destinatariosValidos = new ArrayList<>();
        List<SMSVO.Removidos> destinatariosRemovidos = new ArrayList<>();

        long totalSmsCalculado = 0;
        long invalidosCount = 0;
        long higienizadosCount = 0;

        for (String[] colunas : linhas) {
            String ddiTelefone = colunas.length > 0 ? colunas[0].trim() : "";
            String dddTelefone = colunas.length > 1 ? colunas[1].trim() : "";
            String telefone = colunas.length > 2 ? colunas[2].trim() : "";
            String mensagemDaPlanilha = colunas.length > 3 ? colunas[3].trim() : "";

            if (colunas.length == 1 && !colunas[0].isEmpty()) {
                String numeroCompleto = colunas[0].trim();
                if (numeroCompleto.matches("\\d{10,11}")) {
                    dddTelefone = numeroCompleto.substring(0, 2);
                    telefone = numeroCompleto.substring(2);
                } else {
                    telefone = numeroCompleto;
                }
            }

            String mensagemFinal = pedido.getConteudoMensagemUnicoBoolean()
                    ? pedido.getConteudoMensagem()
                    : mensagemDaPlanilha;

            StringBuilder motivoInvalidade = new StringBuilder();
            boolean isEspecial = false;
            boolean foiHigienizado = false;

            if (smsFilterService.isBlocked(mensagemFinal)) {
                motivoInvalidade.append(MOTIVO_CONTEUDO_BLOQUEADO);
                foiHigienizado = true; // Marcamos como higienizado
            }

            if (telefone.isEmpty() || !telefone.matches("\\d{8,9}")) {
                motivoInvalidade.append(MOTIVO_TELEFONE_INVALIDO);
            }
            if (dddTelefone.isEmpty() || !dddTelefone.matches("\\d{2}")) {
                motivoInvalidade.append(MOTIVO_DDD_INVALIDO);
            }
            if (mensagemFinal == null || mensagemFinal.trim().isEmpty()) {
                motivoInvalidade.append(MOTIVO_MENSAGEM_VAZIA);
            } else {
                isEspecial = contemCaracteresNaoGsm(mensagemFinal);
                int maxChars = isEspecial
                        ? UNICODE_CONCATENATED_SMS_LIMIT * MAX_SMS_PARTS
                        : GSM_CONCATENATED_SMS_LIMIT * MAX_SMS_PARTS;
                if (mensagemFinal.length() > maxChars) {
                    motivoInvalidade.append(String.format(MOTIVO_MENSAGEM_MUITO_LONGA, maxChars));
                }
            }

            if (motivoInvalidade.length() == 0) {
                int quantidadePartes = calcularPartesSms(mensagemFinal, isEspecial);
                totalSmsCalculado += quantidadePartes;
                SMSVO.Destinatario novoDestinatario = new SMSVO.Destinatario();
                if (telefone.length() == 8) telefone = "9" + telefone;
                if (ddiTelefone.isEmpty() || !ddiTelefone.equals("55")) ddiTelefone = "55";
                String telefoneCompleto = ddiTelefone + dddTelefone + telefone;
                novoDestinatario.setNumero(telefoneCompleto);
                novoDestinatario.setMensagem(mensagemFinal);
                novoDestinatario.setQuantidadeCaratecteresMensagem(String.valueOf(mensagemFinal.length()));
                novoDestinatario.setQuantidadeSMS(String.valueOf(quantidadePartes));
                destinatariosValidos.add(novoDestinatario);

            } else {
                SMSVO.Removidos destinatarioRemovido = new SMSVO.Removidos();

                if (foiHigienizado) {
                    destinatarioRemovido.setMotivo(2);
                    higienizadosCount++;
                } else {
                    destinatarioRemovido.setMotivo(1);
                    invalidosCount++;
                }

                if (ddiTelefone.isEmpty()) ddiTelefone = "**";
                if (dddTelefone.isEmpty()) dddTelefone = "**";
                if (telefone.isEmpty()) telefone = "* ****-****";
                String telefoneCompleto = ddiTelefone + dddTelefone + telefone;
                destinatarioRemovido.setNumero(telefoneCompleto);
                destinatarioRemovido.setMensagem(motivoInvalidade.toString().trim());
                destinatariosRemovidos.add(destinatarioRemovido);
            }
        }

        SistelcomDTO sistelcomDTO = new SistelcomDTO();
        long validosCount = destinatariosValidos.size();

        sistelcomDTO.setValidos(validosCount);
        sistelcomDTO.setInvalidos(invalidosCount);
        sistelcomDTO.setHigienizados(higienizadosCount);
        sistelcomDTO.setTotal(validosCount + invalidosCount + higienizadosCount);
        sistelcomDTO.setQuantidadeSMS(totalSmsCalculado);
        sistelcomDTO.setSmsDestinatarios(destinatariosValidos);
        sistelcomDTO.setSmsRemovidos(destinatariosRemovidos);

        logger.info("Processamento finalizado. Válidos: {}, Inválidos: {}, Higienizados: {}, Total de SMS: {}",
                validosCount, invalidosCount, higienizadosCount, totalSmsCalculado);
        return sistelcomDTO;
    }

    private boolean contemCaracteresNaoGsm(String mensagem) {
        if (mensagem == null || mensagem.isEmpty()) {
            return false;
        }
        return !mensagem.matches(SMS_SIMPLES_REGEX);
    }

    private int calcularPartesSms(String mensagem, boolean isEspecial) {
        int length = mensagem.length();
        if (length == 0) return 0;
        if (isEspecial) {
            if (length <= UNICODE_SINGLE_SMS_LIMIT) return 1;
            return (int) Math.ceil((double) length / UNICODE_CONCATENATED_SMS_LIMIT);
        } else {
            if (length <= GSM_SINGLE_SMS_LIMIT) return 1;
            return (int) Math.ceil((double) length / GSM_CONCATENATED_SMS_LIMIT);
        }
    }

}
