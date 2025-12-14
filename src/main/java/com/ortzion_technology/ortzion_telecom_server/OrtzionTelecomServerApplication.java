package com.ortzion_technology.ortzion_telecom_server;

import com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter.BancoInterConfiguration;
import com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter.BancoInterService;
import com.ortzion_technology.ortzion_telecom_server.configuration.pagarme_stone.PagarmeConfiguration;
import com.ortzion_technology.ortzion_telecom_server.configuration.pingoo_mobi.PingooMobiConfiguration;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.CanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.ItemPacoteCanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.PacoteCanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.cadastral.RolesEnum;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.ColaboradorRepository;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.EmpresaRepository;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.PessoaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.entity.*;
import com.ortzion_technology.ortzion_telecom_server.security.repository.*;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.CanalMensageriaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.PacoteCanalMensageriaService;
import com.ortzion_technology.ortzion_telecom_server.security.service.IpCredenciadoApiPagarmeSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;
import java.util.*;

@SpringBootApplication
@EnableConfigurationProperties({PagarmeConfiguration.class, PingooMobiConfiguration.class, BancoInterConfiguration.class})
@EnableScheduling
@EnableAsync
public class OrtzionTelecomServerApplication implements ApplicationRunner, CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(OrtzionTelecomServerApplication.class);

    private final RoleRepository roleRepository;
    private final AcessoFuncionalidadeRepository acessoFuncionalidadeRepository;
    private final AcessoGrupoRepository acessoGrupoRepository;
    private final AcessoGrupoFuncionalidadeRepository acessoGrupoFuncionalidadeRepository;
    private final CanalMensageriaService canalMensageriaService;
    private final PacoteCanalMensageriaService pacoteCanalMensageriaService;
    private final IpCredenciadoApiPagarmeSecurityService ipCredenciadoApiPagarmeSecurityService;
    private final DepartamentoRepository departamentoRepository;
    private final EmpresaRepository empresaRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final PessoaRepository pessoaRepository;
    private final AcessoUsuarioRepository acessoUsuarioRepository;
    private final MulticontaRepository multicontaRepository;
    private final BancoInterService bancoInterService;
    private final BancoInterConfiguration bancoInterConfiguration;

    public OrtzionTelecomServerApplication(RoleRepository roleRepository, AcessoFuncionalidadeRepository acessoFuncionalidadeRepository, AcessoGrupoRepository acessoGrupoRepository, AcessoGrupoFuncionalidadeRepository acessoGrupoFuncionalidadeRepository, CanalMensageriaService canalMensageriaService, PacoteCanalMensageriaService pacoteCanalMensageriaService, IpCredenciadoApiPagarmeSecurityService ipCredenciadoApiPagarmeSecurityService, DepartamentoRepository departamentoRepository, EmpresaRepository empresaRepository, ColaboradorRepository colaboradorRepository, PessoaRepository pessoaRepository, AcessoUsuarioRepository acessoUsuarioRepository, MulticontaRepository multicontaRepository, BancoInterService bancoInterService, BancoInterConfiguration bancoInterConfiguration) {
        this.roleRepository = roleRepository;
        this.acessoFuncionalidadeRepository = acessoFuncionalidadeRepository;
        this.acessoGrupoRepository = acessoGrupoRepository;
        this.acessoGrupoFuncionalidadeRepository = acessoGrupoFuncionalidadeRepository;
        this.canalMensageriaService = canalMensageriaService;
        this.pacoteCanalMensageriaService = pacoteCanalMensageriaService;
        this.ipCredenciadoApiPagarmeSecurityService = ipCredenciadoApiPagarmeSecurityService;
        this.departamentoRepository = departamentoRepository;
        this.empresaRepository = empresaRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.pessoaRepository = pessoaRepository;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.multicontaRepository = multicontaRepository;
        this.bancoInterService = bancoInterService;
        this.bancoInterConfiguration = bancoInterConfiguration;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrtzionTelecomServerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("ApplicationRunner executado!");

        // --- ROLES, FUNCIONALIDADES, GRUPOS (MANTIDO IGUAL) ---
        Role roleAdmin = this.roleRepository.findById(RolesEnum.ROLE_ADMIN.getCodigoNumerico()).orElseGet(() -> {
            return this.roleRepository.save(new Role(null, "ROLE_ADMIN"));
        });

        Role roleUser = this.roleRepository.findById(RolesEnum.ROLE_USER.getCodigoNumerico()).orElseGet(() -> {
            return this.roleRepository.save(new Role(null, "ROLE_USER"));
        });

        AcessoFuncionalidade acessoFuncionalidadeAdmin = this.acessoFuncionalidadeRepository.findById(RolesEnum.ROLE_ADMIN.getCodigoNumerico()).orElseGet(() -> {
            return this.acessoFuncionalidadeRepository.save(new AcessoFuncionalidade(null, "Acesso funcionalidade administrador sistema", roleAdmin));
        });

        AcessoFuncionalidade acessoFuncionalidadeUser = this.acessoFuncionalidadeRepository.findById(RolesEnum.ROLE_USER.getCodigoNumerico()).orElseGet(() -> {
            return this.acessoFuncionalidadeRepository.save(new AcessoFuncionalidade(null, "Acesso funcionalidade usuario sistema", roleUser));
        });

        AcessoGrupo acessoGrupoAdmin = this.acessoGrupoRepository.findById(RolesEnum.ROLE_ADMIN.getCodigoNumerico()).orElseGet(() -> {
            return this.acessoGrupoRepository.save(new AcessoGrupo(null, "PERFIL_ADMINISTRADOR", null, null));
        });

        AcessoGrupo acessoGrupoUser = this.acessoGrupoRepository.findById(RolesEnum.ROLE_USER.getCodigoNumerico()).orElseGet(() -> {
            return this.acessoGrupoRepository.save(new AcessoGrupo(null, "PERFIL_USUARIO", null, null));
        });

        AcessoGrupoFuncionalidadeId adminFuncId = new AcessoGrupoFuncionalidadeId(acessoGrupoAdmin.getIdGrupo(), acessoFuncionalidadeAdmin.getIdFuncionalidadeUsuario());
        this.acessoGrupoFuncionalidadeRepository.findById(adminFuncId).orElseGet(() -> {
            AcessoGrupoFuncionalidade agf = new AcessoGrupoFuncionalidade();
            agf.setAcessoFuncionalidadeId(adminFuncId);
            agf.setAcessoGrupo(acessoGrupoAdmin);
            agf.setAcessoFuncionalidade(acessoFuncionalidadeAdmin);
            return this.acessoGrupoFuncionalidadeRepository.save(agf);
        });

        AcessoGrupoFuncionalidadeId userFuncId = new AcessoGrupoFuncionalidadeId(acessoGrupoUser.getIdGrupo(), acessoFuncionalidadeUser.getIdFuncionalidadeUsuario());
        this.acessoGrupoFuncionalidadeRepository.findById(userFuncId).orElseGet(() -> {
            AcessoGrupoFuncionalidade agf = new AcessoGrupoFuncionalidade();
            agf.setAcessoFuncionalidadeId(userFuncId);
            agf.setAcessoGrupo(acessoGrupoUser);
            agf.setAcessoFuncionalidade(acessoFuncionalidadeUser);
            return this.acessoGrupoFuncionalidadeRepository.save(agf);
        });

        // --- CANAIS DE MENSAGERIA ---
        CanalMensageria canalMensageriaSMS = this.canalMensageriaService.pegarCanalMensageriaPorId(1).orElseGet(() -> {
            CanalMensageria canal = new CanalMensageria();
            canal.setNomeCanalMensageria("SMS");
            canal.setDescricaoCanalMensageria("CANAL DE MENSAGERIA SMS");
            canal.setPrecoCompra(BigDecimal.valueOf(0.05));
            canal.setPrecoMinimoVenda(BigDecimal.valueOf(0.08));
            return this.canalMensageriaService.salvarCanalMensageria(canal);
        });

        CanalMensageria canalMensageriaEMAIL = this.canalMensageriaService.pegarCanalMensageriaPorId(2).orElseGet(() -> {
            CanalMensageria canal = new CanalMensageria();
            canal.setNomeCanalMensageria("EMAIL");
            canal.setDescricaoCanalMensageria("CANAL DE MENSAGERIA EMAIL");
            canal.setPrecoCompra(BigDecimal.valueOf(0.20));
            canal.setPrecoMinimoVenda(BigDecimal.valueOf(0.20));
            return this.canalMensageriaService.salvarCanalMensageria(canal);
        });

        CanalMensageria canalMensageriaWHATSAPP = this.canalMensageriaService.pegarCanalMensageriaPorId(3).orElseGet(() -> {
            CanalMensageria canal = new CanalMensageria();
            canal.setNomeCanalMensageria("WHATSAPP");
            canal.setDescricaoCanalMensageria("CANAL DE MENSAGERIA WHATSAPP");
            canal.setPrecoCompra(BigDecimal.valueOf(0.80));
            canal.setPrecoMinimoVenda(BigDecimal.valueOf(0.80));
            return this.canalMensageriaService.salvarCanalMensageria(canal);
        });

        // --- PACOTES (COM CORREÇÃO DE VÍNCULO PAI-FILHO) ---
        // A correção é: item.setPacoteCanalMensageria(pacote);

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(1).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-BASIC");
            pacote.setTipoPacoteCanalMensageria(1);
            pacote.setDescricoes(Set.of("1.000 SMS"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(120));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.12));

            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaSMS);
            item.setQuantidade(1000L);
            item.setPacoteCanalMensageria(pacote); // <--- VINCULO OBRIGATÓRIO

            pacote.setItemPacoteCanalMensageria(List.of(item));
            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(2).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-SILVER");
            pacote.setTipoPacoteCanalMensageria(1);
            pacote.setDescricoes(Set.of("2000 SMS"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(200));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.10));

            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaSMS);
            item.setQuantidade(2000L);
            item.setPacoteCanalMensageria(pacote); // <--- VINCULO

            pacote.setItemPacoteCanalMensageria(List.of(item));
            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(3).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-GOLD");
            pacote.setTipoPacoteCanalMensageria(1);
            pacote.setDescricoes(Set.of("10000 SMS"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(900));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.09));

            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaSMS);
            item.setQuantidade(10000L);
            item.setPacoteCanalMensageria(pacote); // <--- VINCULO

            pacote.setItemPacoteCanalMensageria(List.of(item));
            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(4).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-Emerald");
            pacote.setTipoPacoteCanalMensageria(1);
            pacote.setDescricoes(Set.of("50.000 SMS"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(4250));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.085));

            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaSMS);
            item.setQuantidade(50000L);
            item.setPacoteCanalMensageria(pacote); // <--- VINCULO

            pacote.setItemPacoteCanalMensageria(List.of(item));
            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(5).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-BLUE SAPPHIRE");
            pacote.setTipoPacoteCanalMensageria(1);
            pacote.setDescricoes(Set.of("100.000 SMS"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(8500));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.085));

            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaSMS);
            item.setQuantidade(100000L);
            item.setPacoteCanalMensageria(pacote); // <--- VINCULO

            pacote.setItemPacoteCanalMensageria(List.of(item));
            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(6).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-RED DIAMOND");
            pacote.setTipoPacoteCanalMensageria(1);
            pacote.setDescricoes(Set.of("1.000.000 SMS"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(80000));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.08));

            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaSMS);
            item.setQuantidade(1000000L);
            item.setPacoteCanalMensageria(pacote); // <--- VINCULO

            pacote.setItemPacoteCanalMensageria(List.of(item));
            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        // PACOTES AVULSOS (TIPO 2)
        // Nota: Se eles forem avulsos, talvez não precisem de quantidade fixa ou item pacote,
        // mas para manter padrão vamos criar um item com qtd 1 ou similar.

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(7).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-INDIVIDUAL SMS");
            pacote.setTipoPacoteCanalMensageria(2);
            pacote.setDescricoes(Set.of("SMS"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(0.12));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.12));
            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaSMS);
            item.setQuantidade(1L);
            item.setPacoteCanalMensageria(pacote);
            pacote.setItemPacoteCanalMensageria(List.of(item));
            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(8).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-INDIVIDUAL EMAIL");
            pacote.setTipoPacoteCanalMensageria(2);
            pacote.setDescricoes(Set.of("EMAIL"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(0.20));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.20));
            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaEMAIL);
            item.setQuantidade(1L);
            item.setPacoteCanalMensageria(pacote);
            pacote.setItemPacoteCanalMensageria(List.of(item));

            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        this.pacoteCanalMensageriaService.pegarPacoteCanalMensageriaPorId(9).orElseGet(() -> {
            PacoteCanalMensageria pacote = new PacoteCanalMensageria();
            pacote.setNomePacoteCanalMensageria("K-INDIVIDUAL WHATSAPP");
            pacote.setTipoPacoteCanalMensageria(2);
            pacote.setDescricoes(Set.of("WHATSAPP"));
            pacote.setPrecoPacoteCanalVenda(BigDecimal.valueOf(0.75));
            pacote.setPrecoUnitarioVenda(BigDecimal.valueOf(0.75));
            ItemPacoteCanalMensageria item = new ItemPacoteCanalMensageria();
            item.setCanalMensageria(canalMensageriaWHATSAPP);
            item.setQuantidade(1L);
            item.setPacoteCanalMensageria(pacote);
            pacote.setItemPacoteCanalMensageria(List.of(item));

            return this.pacoteCanalMensageriaService.salvarPacoteCanalMensageria(pacote);
        });

        // --- INICIALIZAÇÃO IPs PAGARME ---
        initializePagarmeIps();

        // --- INICIALIZAÇÃO WEBHOOK INTER ---
        initializeWebhookInter();

        // --- DEPARTAMENTOS ---
        this.departamentoRepository.findById(1).orElseGet(() -> {
            Departamento depto = new Departamento();
            depto.setCodigoDepartamento("DPP1");
            depto.setNomeDepartamento("DEPARTAMENTO PESSOA FÍSICA");
            return this.departamentoRepository.save(depto);
        });

        this.departamentoRepository.findById(2).orElseGet(() -> {
            Departamento depto = new Departamento();
            depto.setCodigoDepartamento("DPE1");
            depto.setNomeDepartamento("DEPARTAMENTO PESSOA JURÍDICA");
            return this.departamentoRepository.save(depto);
        });
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner executado!");
    }

    private void initializePagarmeIps() {
        final List<String> requiredIps = List.of("52.186.34.80/28", "104.45.183.192/28", "52.186.34.84");
        System.out.println("Verificando integridade dos IPs essenciais da Pagar.me...");
        requiredIps.forEach(this.ipCredenciadoApiPagarmeSecurityService::salvarIpSeNaoExistir);
        System.out.println("Verificação e persistência inicial dos IPs Pagar.me concluída.");
    }

    private void initializeWebhookInter() {
        try {
            String chavePix = bancoInterConfiguration.chavePix();
            String webhookUrl = bancoInterConfiguration.webhookUrl();

            if (chavePix == null || chavePix.isEmpty() || webhookUrl == null || webhookUrl.isEmpty()) {
                logger.warn("BANCO INTER: Chave Pix ou Webhook URL não configurados. Pulando.");
                return;
            }

            logger.info("BANCO INTER: Verificando Webhook para chave: {}", chavePix);
            String webhookExistente = bancoInterService.consultarWebhook(chavePix);

            if (webhookExistente != null) {
                logger.info("BANCO INTER: Webhook JÁ EXISTE configurado para: {}", webhookExistente);
                if (!webhookExistente.equals(webhookUrl)) {
                    logger.warn("ATENÇÃO: URL do Webhook no banco difere da configuração.");
                }
            } else {
                logger.info("BANCO INTER: Webhook NÃO ENCONTRADO. Cadastrando: {}", webhookUrl);
                bancoInterService.configurarWebhook(chavePix, webhookUrl);
                logger.info("BANCO INTER: Webhook cadastrado com SUCESSO!");
            }
        } catch (Exception e) {
            logger.error("BANCO INTER: Falha ao inicializar Webhook: {}", e.getMessage());
        }
    }

}