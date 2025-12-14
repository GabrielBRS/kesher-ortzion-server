# ----- Estágio 1: Build (com Gradle) -----
# Usa a imagem oficial do Gradle com JDK 21
FROM gradle:8.9.0-jdk21 as builder

WORKDIR /app

# Torna o wrapper do Gradle executável
COPY gradlew ./
RUN chmod +x ./gradlew

# Copia os arquivos de configuração do build
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ ./gradle/

# Baixa as dependências primeiro para cachear a camada
RUN ./gradlew dependencies --no-daemon

# Copia o código-fonte
COPY src/ ./src/

# Constrói a aplicação e gera o .jar, pulando os testes
RUN ./gradlew build --no-daemon -x test


# ----- Estágio 2: Runtime (Aplicação Final) -----
# Usa uma imagem JRE 21 mínima (baseada em Alpine)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia APENAS o .jar compilado do estágio 'builder'
# O Gradle salva os artefatos em 'build/libs/'
COPY --from=builder /app/build/libs/*.jar app.jar

# ----- 💡 CORREÇÃO 💡 -----
# Expõe a porta que a sua aplicação usa
EXPOSE 28080

# Define o comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]