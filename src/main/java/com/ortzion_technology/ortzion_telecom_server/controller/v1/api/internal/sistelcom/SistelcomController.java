package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.sistelcom;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.EstoqueMercadoriaVirtualDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.EstoqueMercadoriaVirtualService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.SistelcomService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.TransformadorArquivosMetadosService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/sistelcom")
public class SistelcomController {

    private final SistelcomService sistelcomService;
    private final EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService;
    private final TransformadorArquivosMetadosService transformadorArquivosMetadosService;
    private final SecurityService securityService;

    public SistelcomController(SistelcomService sistelcomService, EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService, TransformadorArquivosMetadosService transformadorArquivosMetadosService, SecurityService securityService) {
        this.sistelcomService = sistelcomService;
        this.estoqueMercadoriaVirtualService = estoqueMercadoriaVirtualService;
        this.transformadorArquivosMetadosService = transformadorArquivosMetadosService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/canal-mensageria/sms",
            method = RequestMethod.POST,
            produces = "application/zip")
    public ResponseEntity<?> processarSMS(
            @RequestPart(value = "pedidoDisparoCanalMensageriaSMSRequest", required = true) PedidoDisparoCanalMensageriaSMSRequest pedidoDisparoCanalMensageriaSMSRequest,
            @RequestPart(value = "listaSMSEnviar", required = true) MultipartFile listaSMSEnviar){

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, pedidoDisparoCanalMensageriaSMSRequest.getMulticonta());

        EstoqueMercadoriaVirtualDTO estoqueMercadoriaVirtualDTO = new EstoqueMercadoriaVirtualDTO(pedidoDisparoCanalMensageriaSMSRequest);

        EstoqueMercadoriaVirtual estoqueMercadoriaVirtual = this.estoqueMercadoriaVirtualService.pegarEstoquesMulticontaPorCanalMensageria(estoqueMercadoriaVirtualDTO, usuario);

        if(estoqueMercadoriaVirtual == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            SistelcomDTO contatosLista = this.transformadorArquivosMetadosService.transformarArquivoParaEnviarSMS(listaSMSEnviar, pedidoDisparoCanalMensageriaSMSRequest);

            if(estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria() < contatosLista.getQuantidadeSMS()){
                String erroMsg = "Erro de validação: O número de contatos na lista (" + contatosLista.getQuantidadeSMS() + ") excede o estoque de SMS disponível (" + estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria() + ").";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroMsg);
            }

            byte[] zipContents = sistelcomService.agendarEnvioEmLote(pedidoDisparoCanalMensageriaSMSRequest, contatosLista, estoqueMercadoriaVirtual, usuario);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/zip"));

            String filename = "relatorio_sms" + LocalDate.now().toString() + ".zip";

            headers.setContentDispositionFormData(filename, filename);

            return new ResponseEntity<>(zipContents, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar envio de mensagens: " + e.getMessage());
        }

    }

//        @RequestMapping(
//            value = "/canal-mensageria/email",
//            method = RequestMethod.POST)
//    public ResponseEntity<?> processarEmail(
//            @RequestPart(value = "pedidoDisparoCanalMensageriaEmailRequest", required = true) PedidoDisparoCanalMensageriaEmailRequest pedidoDisparoCanalMensageriaEmailRequest,
//            @RequestPart(value = "listaEmailEnviar", required = true) MultipartFile listaEmailEnviar,
//            @RequestPart(value = "modeloEmailEnviar", required = false) MultipartFile modeloEmailEnviar){
//
//            AcessoUsuario usuario = UserDetailsServiceImpl.getUsuarioLogado();
//
//            if(pedidoDisparoCanalMensageriaEmailRequest.getMulticontaRequestDTO().getIdUsuario() == null
//                    && pedidoDisparoCanalMensageriaEmailRequest.getMulticontaRequestDTO().getTipoPessoa() == null
//                    && pedidoDisparoCanalMensageriaEmailRequest.getMulticontaRequestDTO().getIdSubjectus() == null){
//                return ResponseEntity.badRequest().build();
//            }
//
//            EstoqueMercadoriaVirtualDTO estoqueMercadoriaVirtualDTO = new EstoqueMercadoriaVirtualDTO(pedidoDisparoCanalMensageriaEmailRequest);
//
//            EstoqueMercadoriaVirtual estoqueMercadoriaVirtual = this.estoqueMercadoriaVirtualService.pegarEstoquesMulticontaPorCanalMensageria(estoqueMercadoriaVirtualDTO, usuario);
//
//            if(estoqueMercadoriaVirtual == null){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
//
//            if(estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria() == null){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
//
//            try {
//                SistelcomDTO contatosLista = this.transformadorArquivosMetadosService.transformarArquivoParaEnviarEmail(listaEmailEnviar, pedidoDisparoCanalMensageriaEmailRequest, modeloEmailEnviar);
//
//                if(estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria() < contatosLista.getSmsDestinatarios().size()){
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//                }
//
//                sistelcomService.agendarEnvioEmLote(pedidoDisparoCanalMensageriaEmailRequest, contatosLista, estoqueMercadoriaVirtual, usuario);
//
//                return ResponseEntity.status(200).build();
//
//            } catch (Exception e) {
//                return ResponseEntity.status(500).body("Erro ao processar envio de mensagens: " + e.getMessage());
//            }
//
//    }
//
//    @RequestMapping(
//            value = "/canal-mensageria/whatsapp",
//            method = RequestMethod.POST)
//    public ResponseEntity<?> processarWhatsap(
//            @RequestPart(value = "pedidoDisparoCanalMensageriaWhatsapRequest", required = true) PedidoDisparoCanalMensageriaWhatsapRequest pedidoDisparoCanalMensageriaWhatsapRequest,
//            @RequestPart(value = "listaWhatsapsEnviar", required = true) MultipartFile listaWhatsapsEnviar){
//
//        AcessoUsuario usuario = UserDetailsServiceImpl.getUsuarioLogado();
//
//        if(pedidoDisparoCanalMensageriaWhatsapRequest.getMulticontaRequestDTO().getIdUsuario() == null
//                && pedidoDisparoCanalMensageriaWhatsapRequest.getMulticontaRequestDTO().getTipoPessoa() == null
//                && pedidoDisparoCanalMensageriaWhatsapRequest.getMulticontaRequestDTO().getIdSubjectus() == null){
//            return ResponseEntity.badRequest().build();
//        }
//
//        EstoqueMercadoriaVirtualDTO estoqueMercadoriaVirtualDTO = new EstoqueMercadoriaVirtualDTO(pedidoDisparoCanalMensageriaWhatsapRequest);
//
//        EstoqueMercadoriaVirtual estoqueMercadoriaVirtual = this.estoqueMercadoriaVirtualService.pegarEstoquesMulticontaPorCanalMensageria(estoqueMercadoriaVirtualDTO, usuario);
//
//        if(estoqueMercadoriaVirtual == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        if(estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria() == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        try {
//            SistelcomDTO contatosLista = this.transformadorArquivosMetadosService.transformarArquivoParaEnviarWhatsapp(listaWhatsapsEnviar, pedidoDisparoCanalMensageriaWhatsapRequest);
//
//            if(estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria() < contatosLista.getSmsDestinatarios().size()){
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
//
//            sistelcomService.agendarEnvioEmLote(pedidoDisparoCanalMensageriaWhatsapRequest, contatosLista, estoqueMercadoriaVirtual, usuario);
//
//            return ResponseEntity.status(200).build();
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Erro ao processar envio de mensagens: " + e.getMessage());
//        }
//
//    }

}
