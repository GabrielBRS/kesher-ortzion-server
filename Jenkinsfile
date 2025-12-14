pipeline {
    agent any // Roda no master, que tem 'cat' e 'ssh'

    stages {
        stage('Checkout') {
            steps {
                // Etapa 1: O Jenkins baixa o código (usa a credencial 'jenkins_deploy_git' do Job)
                checkout scm
                echo 'Código-fonte baixado para o workspace do Jenkins.'
            }
        }

        stage('Deploy to Remote Server') {
            steps {
                echo "Iniciando deploy no servidor 192.168.15.50..."

                // 💡 CORREÇÃO FINAL:
                // Usando 'sshagent' (minúsculo) e o ID da credencial
                // correta ('deploy-server-key') que acabamos de criar.
                sshagent(credentials: ['deploy-server-key']) {

                    // Este comando 'sh' agora é executado dentro do wrapper 'sshagent'
                    sh "cat deploy.sh | ssh -o StrictHostKeyChecking=no ortzion-technology@192.168.15.50 'bash'"
                }
            }
        }
    }
}