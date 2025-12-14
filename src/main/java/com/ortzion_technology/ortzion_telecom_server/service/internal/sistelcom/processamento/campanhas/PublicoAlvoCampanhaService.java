package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.PublicoAlvoCampanha;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusEnvioMensagemEnum;
import com.ortzion_technology.ortzion_telecom_server.entity.vo.SMSVO;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.sistelcom.PublicoAlvoCampanhaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicoAlvoCampanhaService {

    private final PublicoAlvoCampanhaRepository pessoaPublicoAlvoRepository;

    public PublicoAlvoCampanhaService(PublicoAlvoCampanhaRepository pessoaPublicoAlvoRepository) {
        this.pessoaPublicoAlvoRepository = pessoaPublicoAlvoRepository;
    }

    public List<PublicoAlvoCampanha> pegarTodasCampanhas(CampanhaMensageria campanhaMensageria) {
        return this.pessoaPublicoAlvoRepository.pegarPublicoAlvoCampanha(campanhaMensageria.getIdCampanhaMensageria());
    }

    public List<PublicoAlvoCampanha> pegarPublicoAlvoCampanha(CampanhaMensageria campanhaMensageria) {
        return this.pessoaPublicoAlvoRepository.pegarPublicoAlvoCampanha(campanhaMensageria.getIdCampanhaMensageria());
    }

    public PublicoAlvoCampanha salvarPublicoAlvoCampanha(PublicoAlvoCampanha publicoAlvoCampanha) {
        return this.pessoaPublicoAlvoRepository.save(publicoAlvoCampanha);
    }

    public PublicoAlvoCampanha atualizarPublicoAlvoCampanha(PublicoAlvoCampanha publicoAlvoCampanha) {
        return this.pessoaPublicoAlvoRepository.save(publicoAlvoCampanha);
    }

    public List<PublicoAlvoCampanha> salvarPublicoAlvoCampanha(List<PublicoAlvoCampanha> publicoAlvoCampanha) {
        return this.pessoaPublicoAlvoRepository.saveAll(publicoAlvoCampanha);
    }

    public List<PublicoAlvoCampanha> atualizarPublicoAlvoCampanha(List<PublicoAlvoCampanha> publicoAlvoCampanha) {
        return this.pessoaPublicoAlvoRepository.saveAll(publicoAlvoCampanha);
    }

    @Transactional
    public List<PublicoAlvoCampanha> pegarPublicoAlvoCampanhaPendentesParaProcessar(CampanhaMensageria campanha, Pageable pageable) {

        List<PublicoAlvoCampanha> publicoAlvoCampanhas = this.pessoaPublicoAlvoRepository.pegarPublicoAlvoCampanhaPendentesParaProcessar(
                StatusEnvioMensagemEnum.PROCESSANDO.getCodigoNumerico(),
                campanha.getIdCampanhaMensageria(),
                LocalDateTime.now(),
                pageable);

        if (publicoAlvoCampanhas.isEmpty()) {
            return publicoAlvoCampanhas;
        }

        for(PublicoAlvoCampanha publicoAlvoCampanha : publicoAlvoCampanhas) {
            publicoAlvoCampanha.setIdStatusMensagem(StatusEnvioMensagemEnum.ENVIANDO.getCodigoNumerico());
            publicoAlvoCampanha.setStatus("ENVIANDO");
        }

        publicoAlvoCampanhas = this.pessoaPublicoAlvoRepository.saveAll(publicoAlvoCampanhas);

        return publicoAlvoCampanhas;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<PublicoAlvoCampanha> criarPublicoAlvoCampanhaValidos(SistelcomDTO sistelcomDTO, PedidoDisparoCanalMensageriaSMSRequest pedidoDisparoCanalMensageriaSMSRequest, CampanhaMensageria novaCampanhaMensageria) {

        List<PublicoAlvoCampanha> novosPublicoAlvoCampanhasDestaCampanha = new ArrayList<>();

        Integer totalDestinatarios = sistelcomDTO.getSmsDestinatarios().size();
        Integer quantidadeLotes = pedidoDisparoCanalMensageriaSMSRequest.getQuantidadeLotes() != null ? pedidoDisparoCanalMensageriaSMSRequest.getQuantidadeLotes() : 1;
        Integer tamanhoLote = (int) Math.ceil((double) totalDestinatarios / quantidadeLotes);

        for(int i = 0; i < totalDestinatarios; i += tamanhoLote){
            int fimDoLote = Math.min(i + tamanhoLote, totalDestinatarios);
            List<SMSVO.Destinatario> loteAtual = sistelcomDTO.getSmsDestinatarios().subList(i, fimDoLote);
            int numeroDoLote = (i / tamanhoLote) + 1;

            for (SMSVO.Destinatario destinatario : loteAtual) {
                PublicoAlvoCampanha novoPublicoAlvoCampanha = new PublicoAlvoCampanha();

                novoPublicoAlvoCampanha.setIdUsuarioEnvio(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdUsuario());
                novoPublicoAlvoCampanha.setTipoPessoa(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getTipoPessoa());
                novoPublicoAlvoCampanha.setIdSubjectus(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdSubjectus());
                novoPublicoAlvoCampanha.setIdDepartamento(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdDepartamento());

                novoPublicoAlvoCampanha.setCampanhaMensageria(novaCampanhaMensageria);
                novoPublicoAlvoCampanha.setDestino(destinatario.getNumero());
                novoPublicoAlvoCampanha.setDataInicio(LocalDateTime.now());
                novoPublicoAlvoCampanha.setDataAgendada(novaCampanhaMensageria.getDataAgendada());
                novoPublicoAlvoCampanha.setDataAgendadaFinal(novaCampanhaMensageria.getDataAgendadaFinal() != null ? novaCampanhaMensageria.getDataAgendadaFinal() : null);
                novoPublicoAlvoCampanha.setIdStatusMensagem(StatusEnvioMensagemEnum.PROCESSANDO.getCodigoNumerico());
                novoPublicoAlvoCampanha.setIdCanalMensageria(novaCampanhaMensageria.getIdCanalMensageria());
                novoPublicoAlvoCampanha.setMensagemDestinatario(destinatario.getMensagem());
                novoPublicoAlvoCampanha.setIdFatiamentoLote(numeroDoLote);
                novoPublicoAlvoCampanha.setSituacao("A ENVIAR");
                novoPublicoAlvoCampanha.setStatus("A ENVIAR");

                novoPublicoAlvoCampanha.setNomeEmissor(novaCampanhaMensageria.getNomeEmissor());
                novoPublicoAlvoCampanha.setNomeDepartamentoEmissor(novaCampanhaMensageria.getNomeDepartamentoEmissor());

                novoPublicoAlvoCampanha.setFornecedor("PINGOO.MOBI");
                novoPublicoAlvoCampanha.setPlataformaEnvio("KESHER ORTZION TECHNOLOGY");

                novoPublicoAlvoCampanha.setCanalMensageria(novaCampanhaMensageria.getCanalMensageria());
                novoPublicoAlvoCampanha.setDescricaoCampanhaMensageria(novaCampanhaMensageria.getCampanha());

                novosPublicoAlvoCampanhasDestaCampanha.add(novoPublicoAlvoCampanha);
            }
        }
        return novosPublicoAlvoCampanhasDestaCampanha;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void atualizarPublicoAlvoCampanhaInvalidosOuHigienizados(SistelcomDTO sistelcomDTO, PedidoDisparoCanalMensageriaSMSRequest pedidoDisparoCanalMensageriaSMSRequest, CampanhaMensageria novaCampanhaMensageria, List<PublicoAlvoCampanha> novosPublicoAlvoCampanhasDestaCampanha) {
        for (var destinatario : sistelcomDTO.getSmsRemovidos()) {
            PublicoAlvoCampanha novoPublicoAlvoCampanha = new PublicoAlvoCampanha();

            novoPublicoAlvoCampanha.setCampanhaMensageria(novaCampanhaMensageria);

            novoPublicoAlvoCampanha.setIdUsuarioEnvio(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdUsuario());
            novoPublicoAlvoCampanha.setTipoPessoa(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getTipoPessoa());
            novoPublicoAlvoCampanha.setIdSubjectus(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdSubjectus());
            novoPublicoAlvoCampanha.setIdDepartamento(pedidoDisparoCanalMensageriaSMSRequest.getMulticonta().getIdDepartamento());

            novoPublicoAlvoCampanha.setDestino(destinatario.getNumero());
            novoPublicoAlvoCampanha.setDataInicio(LocalDateTime.now());
            novoPublicoAlvoCampanha.setDataAgendada(novaCampanhaMensageria.getDataAgendada());
            novoPublicoAlvoCampanha.setDataAgendadaFinal(novaCampanhaMensageria.getDataAgendadaFinal() != null ? novaCampanhaMensageria.getDataAgendadaFinal() : null);
            novoPublicoAlvoCampanha.setIdCanalMensageria(novaCampanhaMensageria.getIdCanalMensageria());
            novoPublicoAlvoCampanha.setMensagemDestinatario(destinatario.getMensagem());
            novoPublicoAlvoCampanha.setIdFatiamentoLote(0);
            novoPublicoAlvoCampanha.setIdStatusMensagem(destinatario.getMotivo() == 1 ? StatusEnvioMensagemEnum.INVALIDO.getCodigoNumerico() : StatusEnvioMensagemEnum.HIGIENIZADO.getCodigoNumerico());
            novoPublicoAlvoCampanha.setSituacao(destinatario.getMotivo() == 1 ? "Inválido" : "Higienizado");
            novoPublicoAlvoCampanha.setStatus(destinatario.getMotivo() == 1 ? "Inválido" : "Higienizado");

            novoPublicoAlvoCampanha.setNomeEmissor(novaCampanhaMensageria.getNomeEmissor());
            novoPublicoAlvoCampanha.setNomeDepartamentoEmissor(novaCampanhaMensageria.getNomeDepartamentoEmissor());

            novoPublicoAlvoCampanha.setFornecedor("PINGOO.MOBI");
            novoPublicoAlvoCampanha.setPlataformaEnvio("KESHER ORTZION TECHNOLOGY");

            novoPublicoAlvoCampanha.setCanalMensageria(novaCampanhaMensageria.getCanalMensageria());
            novoPublicoAlvoCampanha.setDescricaoCampanhaMensageria(novaCampanhaMensageria.getCampanha());

            novosPublicoAlvoCampanhasDestaCampanha.add(novoPublicoAlvoCampanha);
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void marcarPendentesComoFalha(Long idCampanha) {
        this.pessoaPublicoAlvoRepository.marcarPendentesComoFalha(
                idCampanha,
                StatusEnvioMensagemEnum.FALHOU.getCodigoNumerico(),
                StatusEnvioMensagemEnum.PROCESSANDO.getCodigoNumerico(),
                StatusEnvioMensagemEnum.ENVIANDO.getCodigoNumerico(),
                LocalDateTime.now()
        );
    }

}
