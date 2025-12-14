-- *************************************************************************
-- SCRIPT DE INICIALIZAÇÃO DO POSTGRESQL (EXECUTADO APENAS NA PRIMEIRA VEZ)
-- *************************************************************************

-- 1. CRIAÇÃO DOS ESQUEMAS (SCHEMAS)
-- O 'CREATE SCHEMA IF NOT EXISTS' garante que o comando só rode se o schema não existir.
CREATE SCHEMA IF NOT EXISTS cadastral;
CREATE SCHEMA IF NOT EXISTS mercado_virtual;
CREATE SCHEMA IF NOT EXISTS security;
CREATE SCHEMA IF NOT EXISTS power_business_intelligence;
CREATE SCHEMA IF NOT EXISTS registro_acervo_documentos;
CREATE SCHEMA IF NOT EXISTS suporte;
CREATE SCHEMA IF NOT EXISTS sistema_telecomunicacoes;

-- 2. CONCESSÃO DE PERMISSÕES (GRANT USAGE e CREATE)
-- Concede ao seu usuário DBA as permissões necessárias para interagir com os schemas
-- (USAGE) e para criar tabelas DENTRO deles (CREATE).
GRANT USAGE, CREATE ON SCHEMA cadastral TO ortzion_dba;
GRANT USAGE, CREATE ON SCHEMA mercado_virtual TO ortzion_dba;
GRANT USAGE, CREATE ON SCHEMA security TO ortzion_dba;
GRANT USAGE, CREATE ON SCHEMA power_business_intelligence TO ortzion_dba;
GRANT USAGE, CREATE ON SCHEMA registro_acervo_documentos TO ortzion_dba;
GRANT USAGE, CREATE ON SCHEMA suporte TO ortzion_dba;
GRANT USAGE, CREATE ON SCHEMA sistema_telecomunicacoes TO ortzion_dba;

-- 3. GARANTE PERMISSÕES FUTURAS (DEFAULT PRIVILEGES)
-- Garante que quaisquer NOVAS tabelas ou objetos criados pelo 'ortzion_dba'
-- dentro destes schemas também tenham permissões corretas para ele mesmo (ALL ON TABLES TO ortzion_dba).
ALTER DEFAULT PRIVILEGES IN SCHEMA cadastral GRANT ALL ON TABLES TO ortzion_dba;
ALTER DEFAULT PRIVILEGES IN SCHEMA mercado_virtual GRANT ALL ON TABLES TO ortzion_dba;
ALTER DEFAULT PRIVILEGES IN SCHEMA security GRANT ALL ON TABLES TO ortzion_dba;
ALTER DEFAULT PRIVILEGES IN SCHEMA power_business_intelligence GRANT ALL ON TABLES TO ortzion_dba;
ALTER DEFAULT PRIVILEGES IN SCHEMA registro_acervo_documentos GRANT ALL ON TABLES TO ortzion_dba;
ALTER DEFAULT PRIVILEGES IN SCHEMA suporte GRANT ALL ON TABLES TO ortzion_dba;
ALTER DEFAULT PRIVILEGES IN SCHEMA sistema_telecomunicacoes GRANT ALL ON TABLES TO ortzion_dba;