#!/bin/bash
# deploy.sh
# Roda DENTRO DO SERVIDOR DE DEPLOY (192.168.15.50)

echo "--- [DEPLOY SCRIPT] Iniciando no servidor $(hostname) ---"
set -e # Aborta o script imediatamente se qualquer comando falhar

# --- Configuração ---
APP_DIR="/home/ortzion-technology/ortzion-telecom-server" # Diretório raiz da aplicação no servidor
GIT_BRANCH="master"  # Ajuste para "master" ou "main"
GIT_REMOTE="origin"

# --- Verificação de Pré-requisitos ---
if ! command -v git &> /dev/null; then
    echo "[ERRO] Git não encontrado."
    exit 1
fi
if ! command -v docker &> /dev/null || ! command -v docker compose &> /dev/null ; then
    echo "[ERRO] Docker ou Docker Compose não encontrado."
    exit 1
fi

# --- Navegação e Atualização do Código ---
echo "Navegando para o diretório da aplicação: $APP_DIR"
cd "$APP_DIR" || { echo "[ERRO] Falha ao entrar no diretório $APP_DIR!"; exit 1; }

echo "Atualizando repositório Git (fetch + reset)..."
git fetch "$GIT_REMOTE"
git checkout "$GIT_BRANCH"
git reset --hard "$GIT_REMOTE/$GIT_BRANCH"
git clean -fdx

echo "Código atualizado."

# --- Docker Compose ---
echo "Parando e removendo containers antigos..."
docker compose down --remove-orphans

echo "Construindo e iniciando novos containers..."
docker compose up -d --build --remove-orphans

echo "Containers iniciados."
docker image prune -f
echo "--- [DEPLOY SCRIPT] Deploy concluído com sucesso! ---"
exit 0