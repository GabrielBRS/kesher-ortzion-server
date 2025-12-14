package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.external.pingoo_mobi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SmsStatusPingooMobiResponse {

    @JsonProperty("ddd_telefone")
    private String dddTelefone;

    @JsonProperty("mensagem")
    private String mensagem;

    @JsonProperty("quantidade_de_sms")
    private String quantidadeSms;

    @JsonProperty("campo_informado")
    private String campoInformado;

    @JsonProperty("linha_do_arquivo")
    private String linhaArquivo;

    @JsonProperty("numero_da_linha")
    private String numeroLinha;

    @JsonProperty("data_agendada")
    private String dataAgendada;

    @JsonProperty("data_cadastro")
    private String dataCadastro;

    @JsonProperty("status")
    private String status;

    @JsonProperty("observacao_do_status")
    private String observacaoStatus;

    @JsonProperty("data_do_status")
    private String dataStatus;

    @JsonProperty("nome_da_empresa")
    private String nomeEmpresa;

    @JsonProperty("nome_do_centro_de_custo")
    private String nomeCentroCusto;

    @JsonProperty("codigo_da_empresa")
    private String codigoEmpresa;

    @JsonProperty("fornecedor")
    private String fornecedor;

    @JsonProperty("codigo_do_fornecedor")
    private String codigoFornecedor;

    @JsonProperty("operadora")
    private String operadora;

    @JsonProperty("resposta")
    private String resposta;

    @JsonProperty("data_resposta")
    private String dataResposta;

    @JsonProperty("marcadores")
    private String marcadores;

    @JsonProperty("numero_do_lote")
    private String numeroLote;

    @JsonProperty("quantidade_do_lote")
    private String quantidadeLote;

    @JsonProperty("data_inicio_do_lote")
    private String dataInicioLote;

    @JsonProperty("data_fim_do_lote")
    private String dataFimLote;

    @JsonProperty("nome_da_campanha")
    private String nomeCampanha;

    @JsonProperty("data_criacao_da_campanha")
    private String dataCriacaoCampanha;

    @JsonProperty("data_inicio_da_campanha")
    private String dataInicioCampanha;

    @JsonProperty("data_fim_campanha")
    private String dataFimCampanha;

    @JsonProperty("status_da_campanha")
    private String statusCampanha;

    @JsonProperty("codigo_da_campanha")
    private String codigoCampanha;

    @JsonProperty("total_da_campanha")
    private String totalCampanha;

    @JsonProperty("validos_da_campanha")
    private String validosCampanha;

    @JsonProperty("repetidos_da_campanha")
    private String repetidosCampanha;

    @JsonProperty("invalidos_da_campanha")
    private String invalidosCampanha;

    @JsonProperty("lista_de_bloqueio_da_campanha")
    private String listaBloqueioCampanha;

    @JsonProperty("nome_do_arquivo")
    private String nomeArquivo;

    @JsonProperty("nome_do_layout")
    private String nomeLayout;

    @JsonProperty("codigo_do_layout")
    private String codigoLayout;

    @JsonProperty("nome_da_template")
    private String nomeTemplate;

    @JsonProperty("codigo_da_template")
    private String codigoTemplate;

    @JsonProperty("tipo_do_envio")
    private String tipoEnvio;

    @JsonProperty("tipo_de_tecnologia")
    private String tipoTecnologia;

    @JsonProperty("data_envio")
    private String dataEnvio;

    @JsonProperty("data_confirmacao")
    private String dataConfirmacao;

    @JsonProperty("identificador")
    private String identificador;

    @JsonProperty("identificador_fornecedor")
    private String identificadorFornecedor;

    @JsonProperty("data_atual")
    private String dataAtual;

    @JsonProperty("tipo_de_campanha")
    private String tipoCampanha;

    @JsonProperty("data_envio_padrao")
    private String dataEnvioPadrao;

    @JsonProperty("fornecedor_padrao")
    private String fornecedorPadrao;

    @JsonProperty("id_solucao")
    private String idSolucao;

    @JsonProperty("data_agendada_se_nao_existe_cadastro")
    private String dataAgendadaSeNaoExisteCadastro;

    @JsonProperty("data_envio_se_nao_existe_cadastro")
    private String dataEnvioSeNaoExisteCadastro;

    @JsonProperty("ddd")
    private String ddd;

    @JsonProperty("id_da_mensagem")
    private String idMensagem;

    @JsonProperty("id_da_campanha")
    private String idCampanha;

    @JsonProperty("total_de_enviados_soma")
    private String totalEnviadosSoma;

    @JsonProperty("total_de_entregues_soma")
    private String totalEntreguesSoma;

    @JsonProperty("tamanho_da_mensagem")
    private String tamanhoMensagem;

    @JsonProperty("numero_de_origem")
    private String numeroOrigem;

    @JsonProperty("usuario_que_disparou")
    private String usuarioDisparou;

    @JsonProperty("id_da_empresa")
    private String idEmpresa;

    @JsonProperty("status_interno")
    private String statusInterno;

    @JsonProperty("quantidade_de_respostas_nao_usar_com_outras_respostas")
    private String quantidadeRespostas;

    @JsonProperty("apenas_telefone")
    private String apenasTelefone;

    @JsonProperty("vazio")
    private String vazio;

    @JsonProperty("valor_fixo")
    private String valorFixo;

}
